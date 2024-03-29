package com.robertboothby.djenni.experimental;

import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;

import java.beans.BeanInfo;
import java.beans.FeatureDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.robertboothby.djenni.core.SupplierHelper.fix;
import static com.robertboothby.djenni.lang.IntegerSupplierBuilder.anyInteger;
import static java.util.Arrays.stream;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toSet;

/**
 * This is a dynamic supplier builder that will use reasonable defaults based on the JavaBeans method and constructor parameter naming conventions, but can be massively extended.
 *
 * <em>TO MAKE THE AUTOMATED PARAMETER NAMING WORK, USE -parameters IN THE JAVA COMPILATION. IN INTELLIJ THIS NEEDS TO BE DONE AT THE TOP LEVEL OF THE PROJECT AS WELL AS THE SUB-MODULES!!</em>
 * //TODO don't forget bean setters - We need to add them to the default function if their properties are not already defined by the constructor and only call them if the relevant properties are actually populated.
 * //DONE-TO DO don't forget that not all constructors / methods allow null values. During introspection we may need to allow for dummy values to be supplied. Completed by allowing sample / default values for parameters and suppliers.
 * //TODO don't forget primitive types - while we can get away often with supplying null values for object types we cannot do so for primitive types. We need to create a mechanism to provide a reasonable default.
 *
 * @param <R>
 */
public class DynamicSupplierBuilder<R> implements SupplierBuilder<R> {

    private final Class<R> suppliedClass;
    private final List<Parameter<?>> parameterList = new ArrayList<>();
    private final BeanInfo beanInfo;
    private Function<BuildContext, R> functionThatBuildsTheInstance = $ -> null;
    private Map<String, Function<BuildContext, ?>> instantiationParameterSupplierOverrides = new HashMap<>();
    private Map<String, Function<BuildContext, ?>> setterParameters = new HashMap<>();
    //Dummy object used for introspection;
    private R dummy;


    /**
     * @param suppliedClass The class to be supplied. If it is a class with public constructors, it will automatically
     *                      set the DynamicSupplierBuilder up to use the parameters of the constructor with the longest
     *                      parameter list and any JavaBean properties that are accessed by a setter that are not already
     *                      defined on the constructor. If there is more than one constructor with the same, longest
     *                      parameter list then one of the constructors will be arbitrarily selected; in these circumstances
     *                      it is recommended that the constructor be selected explicitly.
     * @throws IntrospectionException An exception if we are unable to introspect the constructor.
     */
    @SuppressWarnings("unchecked")
    public DynamicSupplierBuilder(Class<R> suppliedClass) throws IntrospectionException {
        this.suppliedClass = suppliedClass;

        stream(suppliedClass.getConstructors())
                .filter($ -> Modifier.isPublic($.getModifiers()))
                .max(comparingInt(Constructor::getParameterCount)) // Get the constructor with the most parameters.
                .ifPresent(constructor -> useDefaultedConstructor((Constructor<R>) constructor));

        beanInfo = Introspector.getBeanInfo(suppliedClass);

        //Add all the setters to set of parameters.
        stream(beanInfo.getPropertyDescriptors())
                .filter($ -> $.getWriteMethod() != null)
                .filter($ -> !parameterList.stream().map(Parameter::getMappedName).collect(toSet()).contains($.getName()))
                .forEach($ -> {
                    parameterList.add(new Parameter<>($.getName(), (Class<Class<?>>) $.getPropertyType()));
                    Function<BuildContext, R> oldFunction = functionThatBuildsTheInstance;
                    functionThatBuildsTheInstance = bc -> {
                        R result = oldFunction.apply(bc);
                        try {
                            //TODO this will not work as expected if there is a default value set on the class that we are overriding to null... needs more thought.
                            Object p = bc.p($.getName(), $.getPropertyType());
                            if (p != null) {
                                $.getWriteMethod().invoke(result, p);
                            }
                        } catch (Exception e) {
                            if (e instanceof RuntimeException) {
                                throw (RuntimeException) e;
                            } else {
                                throw new RuntimeException(e);
                            }
                        }
                        return result;
                    };
                });
    }

    @Override
    public StreamableSupplier<R> build() {
        BuildContext normalBuildContext = new RuntimeBuildContext(parameterList.stream().map(Parameter::new).collect(Collectors.toList()));
        return () -> functionThatBuildsTheInstance.apply(normalBuildContext);
    }

    /**
     * Use this method to select a particular constructor by using the constructor to call it. The parameters of the
     * constructor must be defined using the $ methods that allow this class to identify them. The parameters may not
     * need to be explicitly named as they will be automatically derived from the parameter names of the constructor
     *
     * @param constructorToBeMapped a Function defining the constructor to be mapped using the BuildContext to derive
     *                              the parameters.
     */
    public DynamicSupplierBuilder<R> useConstructor(Function<BuildContext, R> constructorToBeMapped) {
        IntrospectionBuildContext introspectionBuildContext = new IntrospectionBuildContext();
        constructorToBeMapped.apply(introspectionBuildContext);
        this.parameterList.clear();
        this.parameterList.addAll(introspectionBuildContext.getParameterList());
        if (parameterList.stream().anyMatch($ -> $.getMappedName() == null)) { //try to derive the underlying parameter name from a matching constructor.
            try {
                Class<?>[] constructorParameterClasses = parameterList.stream().map(Parameter::getParameterClass).toArray(Class[]::new);
                Constructor<R> constructor = suppliedClass.getConstructor(constructorParameterClasses);
                java.lang.reflect.Parameter[] parameters = constructor.getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    parameterList.get(i).setParameterName(parameters[i].getName());
                }
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("No constructor was identifiable from the parameters called on the supplier," +
                        "did you call additional methods with parameters not in the constructor?", e);
            }
        }
        this.functionThatBuildsTheInstance = constructorToBeMapped;
        return this;
    }

    @SuppressWarnings("unchecked")
    private void useDefaultedConstructor(Constructor<R> constructor) {
        MethodType constructorMethodType = MethodType.methodType(void.class, constructor.getParameterTypes());
        try {
            MethodHandle constructorMethodHandle = MethodHandles.publicLookup().findConstructor(constructor.getDeclaringClass(), constructorMethodType);
            useFunction($ -> {
                try {
                    dummy = (R) constructorMethodHandle.invokeWithArguments(stream(constructor.getParameters()).map(parameter -> $.p(parameter.getName(), parameter.getType())).toArray());
                    return dummy;
                } catch (Throwable throwable) {
                    //Try and avoid unnecessary wrapping.
                    if (throwable instanceof Error) {
                        throw (Error) throwable;
                    } else if (throwable instanceof RuntimeException) {
                        throw (RuntimeException) throwable;
                    } else {
                        throw new RuntimeException(throwable);
                    }
                }
            });
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Use this method to supply an arbitrary function to be used when constructing instances for this supplier. It
     * will not attempt to infer names for the parameters and so you must either explicitly define the names or you can
     * only specify a new Supplier for the parameter by the 'position' of the parameter in the supplied function.
     *
     * @param functionToBeUsed the function to be used when constructing this
     * @return the DynamicSupplierBuilder for further configuration.
     */
    public DynamicSupplierBuilder<R> useFunction(Function<BuildContext, R> functionToBeUsed) {
        IntrospectionBuildContext introspectionBuildContext = new IntrospectionBuildContext();
        functionToBeUsed.apply(introspectionBuildContext);
        this.parameterList.clear();
        this.parameterList.addAll(introspectionBuildContext.getParameterList());
        this.functionThatBuildsTheInstance = functionToBeUsed;
        return this;
    }

    public <T> DynamicSupplierBuilder<R> byGet(IntrospectableFunction<? super R, T> getter, Supplier<T> parameterSupplier) {
        getter(getter).setParameterSupplier(parameterSupplier);
        return this;
    }

    public <T> DynamicSupplierBuilder<R> byGet(IntrospectableFunction<? super R, T> getter, SupplierBuilder<T> parameterSupplier) {
        getter(getter).setParameterSupplier(parameterSupplier.build());
        return this;
    }

    private <T> Parameter<T> getter(IntrospectableFunction<? super R, T> getter) {
        String propertyName = stream(beanInfo.getPropertyDescriptors())
                .filter($ -> $.getReadMethod() != null)
                .filter($ -> $.getReadMethod().getName().equals(getter.getMethodName()))
                .findFirst()
                .map(FeatureDescriptor::getName).orElseThrow();

        return (Parameter<T>) parameterList.stream().filter($ -> $.getMappedName().equals(propertyName)).findFirst().orElseThrow();
    }

    public <T> DynamicSupplierBuilder<R> bySet(IntrospectableBiConsumer<? super R, T> setter, Supplier<T> parameterSupplier) {
        setter(setter).setParameterSupplier(parameterSupplier);
        return this;
    }

    public <T> DynamicSupplierBuilder<R> bySet(IntrospectableBiConsumer<? super R, T> setter, SupplierBuilder<T> parameterSupplier) {
        setter(setter).setParameterSupplier(parameterSupplier.build());
        return this;
    }

    private <T> Parameter<T> setter(IntrospectableBiConsumer<? super R, ? extends T> setter) {
        String propertyName = stream(beanInfo.getPropertyDescriptors())
                .filter($ -> $.getWriteMethod() != null)
                .filter($ -> $.getWriteMethod().getName().equals(setter.getMethodName()))
                .findFirst()
                .map(FeatureDescriptor::getName).orElseThrow();

        return (Parameter<T>) parameterList.stream().filter($ -> $.getMappedName().equals(propertyName)).findFirst().orElseThrow();
    }

    public static <T> DynamicSupplierBuilder<T> supplierFor(Class<T> tClass) throws IntrospectionException {
        return new DynamicSupplierBuilder<>(tClass);
    }

    public <T> void test(Function<? super R, ? extends T> mapper) {

    }


    public static void main(String[] args) throws IntrospectionException {
        //.useConstructor($ -> new TestClass($.p("One"), $.p(Integer.class)));

        StreamableSupplier<TestClass> testClassSupplier = supplierFor(TestClass.class)
                .byGet(TestClass::getValueTwo, anyInteger().between(1).and(10))
                .byGet(TestClass::getValueOne, fix("One"))
                .bySet(TestClass::setValueThree, fix("Three"))
                .build();
        TestClass testClass = testClassSupplier.get();
        //OR Infinite stream
        Stream<TestClass> testClassStream = testClassSupplier.stream();
        //OR Stream of 10
        Stream<TestClass> testClassStreamOfTen = testClassSupplier.stream(10);

        System.out.println("DUMMY");


//        DynamicSupplierBuilder<TestClass> supplierBuilder = new DynamicSupplierBuilder<>(TestClass.class)
//                .byGet($ -> $::getValueTwo, integerSupplier().between(1).and(10))
//                .byGet($ -> $::getValueOne, fix("One"));
    }

    public static class TestClass {
        private final String valueOne;

        private final Integer valueTwo;
        private String valueThree = "";


        public TestClass(String valueOne, Integer valueTwo) {
            this.valueOne = valueOne;
            this.valueTwo = valueTwo;
        }

        public String getValueOne() {
            return valueOne;
        }

        public Integer getValueTwo() {
            return valueTwo;
        }

        public void setValueThree(String valueThree) {
            this.valueThree = valueThree;
        }

    }

    public interface BuildContext {

        /**
         * Use this parameter definition when there is no bad side effect to having a null value for the parameter.
         *
         * @param parameterClass The class of the parameter.
         * @param <P>            The type of the parameter
         * @return a null value.
         */
        <P> P p(Class<P> parameterClass);

        /**
         * Use this parameter definition when there is no bad side effect to having a null value for the parameter and
         * you want to override the name of the parameter to make auto-mapping work or your code more legible.
         *
         * @param name           an override to the default name of the parameter to the constructor to help with the mapping.
         * @param parameterClass The class of the parameter.
         * @param <P>            The type of the parameter
         * @return a null value.
         */
        <P> P p(String name, Class<P> parameterClass);

        /**
         * Use this parameter definition when you want to supply a sample / default value that can be overridden, this
         * is particularly useful when dealing with parameters that cannot take null values.
         *
         * @param fixedParameter a fixed value that can be overridden.
         * @param <P>            The type of the parameter.
         * @return the fixed value.
         */
        <P> P p(P fixedParameter);

        /**
         * Use this parameter definition when you want to supply a sample / default value that can be overridden and
         * you want to override the name of the parameter to make auto-mapping work or your code more legible.
         *
         * @param name           an override to the default name of the parameter to the constructor to help with the mapping.
         * @param fixedParameter a fixed value that can be overridden.
         * @param <P>            The type of the parameter.
         * @return the fixed value.
         */
        <P> P p(String name, P fixedParameter);

        /**
         * Use this parameter definition when you want to supply a default supplier that can be overridden.
         *
         * @param defaultSupplier a default supplier that can be overridden.
         * @param <P>             The type of the parameter.
         * @return the fixed value.
         */
        <P> P p(Supplier<P> defaultSupplier);

        /**
         * Use this parameter definition when you want to supply a default supplier that can be overridden and
         * you want to override the name of the parameter to make auto-mapping work or your code more legible.
         *
         * @param name            an override to the default name of the parameter to the constructor to help with the mapping.
         * @param defaultSupplier a default supplier that can be overridden.
         * @param <P>             The type of the parameter.
         * @return the fixed value.
         */
        <P> P p(String name, Supplier<P> defaultSupplier);
    }

    /**
     * This execution context is used when the build constructor or function is being introspected and collects details of the methods / parameters that are called on the object.
     */
    public class IntrospectionBuildContext implements BuildContext {

        public List<Parameter<?>> getParameterList() {
            return parameterList;
        }

        private final List<Parameter<?>> parameterList = new ArrayList<>();

        public <P> P p(Class<P> parameterClass) {
            return addParameterToList(new Parameter<>(parameterClass));
        }

        public <P> P p(String name, Class<P> parameterClass) {
            return addParameterToList(new Parameter<P>(name, parameterClass));
        }

        public <P> P p(P fixedParameter) {
            return addParameterToList(new Parameter<P>(fixedParameter));
        }

        public <P> P p(String name, P fixedParameter) {
            return addParameterToList(new Parameter<P>(name, fixedParameter));
        }

        @Override
        public <P> P p(Supplier<P> defaultSupplier) {
            return addParameterToList(new Parameter<P>(defaultSupplier));
        }

        @Override
        public <P> P p(String name, Supplier<P> defaultSupplier) {
            return addParameterToList(new Parameter<P>(name, defaultSupplier));
        }

        private <P> P addParameterToList(Parameter<P> parameter) {
            parameterList.add(parameter);
            return parameter.parameterSupplier.get();
        }
    }

    /**
     * This build context is used when we are actually supplying values, using the configured suppliers for the values
     * that have been defined.
     */
    public class RuntimeBuildContext implements BuildContext {

        private final List<? extends Parameter<?>> parameters;
        private int parameterPosition = 0;

        public RuntimeBuildContext(List<? extends Parameter<?>> parameters) {
            this.parameters = parameters;
        }

        @Override
        public <P> P p(Class<P> parameterClass) {
            return (P) parameterList.get(parameterPosition++).getParameterSupplier().get();
        }

        @Override
        public <P> P p(String name, Class<P> parameterClass) {
            return (P) parameterList.get(parameterPosition++).getParameterSupplier().get();
        }

        @Override
        public <P> P p(P fixedParameter) {
            return (P) parameterList.get(parameterPosition++).getParameterSupplier().get();
        }

        @Override
        public <P> P p(String name, P fixedParameter) {
            return (P) parameterList.get(parameterPosition++).getParameterSupplier().get();
        }

        @Override
        public <P> P p(Supplier<P> defaultSupplier) {
            return (P) parameterList.get(parameterPosition++).getParameterSupplier().get();
        }

        @Override
        public <P> P p(String name, Supplier<P> defaultSupplier) {
            return (P) parameterList.get(parameterPosition++).getParameterSupplier().get();
        }
    }

    public static class Parameter<P> {
        private String parameterName;
        private Supplier<P> parameterSupplier = () -> null;
        private String nameOverride;
        private Class<? extends P> parameterClass;

        public Parameter(IntrospectableSupplier<P> parameterSupplier) {
            this(null, parameterSupplier);
        }

        public Parameter(String nameOverride, IntrospectableSupplier<P> parameterSupplier) {
            this.nameOverride = nameOverride;
            this.parameterClass = parameterSupplier.getReturnType();
            this.parameterSupplier = parameterSupplier;
        }

        public Parameter(Class<P> parameterClass) {
            this(null, parameterClass);
        }

        public Parameter(String nameOverride, Class<P> parameterClass) {
            this.nameOverride = nameOverride;
            this.parameterClass = parameterClass;
        }

        public Parameter(P defaultParameterValue) {
            this(null, defaultParameterValue);
        }

        public Parameter(Supplier<P> defaultParameterSupplier) {
            this(null, defaultParameterSupplier);
        }

        @SuppressWarnings("unchecked")
        public Parameter(String nameOverride, P defaultParameterValue) {
            this.nameOverride = nameOverride;
            this.parameterClass = (Class<P>) defaultParameterValue.getClass();
            this.parameterSupplier = () -> defaultParameterValue;
        }

        @SuppressWarnings("unchecked")
        public Parameter(String nameOverride, Supplier<P> defaultParameterSupplier) {
            this.nameOverride = nameOverride;
            this.parameterClass = (Class<P>) defaultParameterSupplier.get().getClass();
            this.parameterSupplier = defaultParameterSupplier;
        }

        public Parameter(Parameter<P> original) {
            this.parameterName = original.parameterName;
            this.parameterSupplier = original.parameterSupplier;
            this.nameOverride = original.nameOverride;
            this.parameterClass = original.parameterClass;
        }


        public String getParameterName() {
            return parameterName;
        }

        public Supplier<P> getParameterSupplier() {
            return parameterSupplier;
        }

        public void setParameterSupplier(Supplier<P> parameterSupplier) {
            this.parameterSupplier = parameterSupplier;
        }

        public Optional<String> getNameOverride() {
            return Optional.ofNullable(nameOverride);
        }

        public String getMappedName() {
            return getNameOverride().orElse(getParameterName());
        }

        public Class<? extends P> getParameterClass() {
            return parameterClass;
        }

        public void setParameterName(String parameterName) {
            this.parameterName = parameterName;
        }
    }

}
