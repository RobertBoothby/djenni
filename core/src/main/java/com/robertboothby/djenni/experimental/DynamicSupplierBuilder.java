package com.robertboothby.djenni.experimental;

import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;

import java.beans.BeanInfo;
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

import static java.util.Arrays.stream;

/**
 * This is a dynamic supplier builder that will use reasonable defaults based on the JavaBeans method and constructor parameter naming conventions, but can be massively extended.
 * //TODO don't forget bean setters.
 *
 * @param <R>
 */
public class DynamicSupplierBuilder<R> implements SupplierBuilder<R> {

    private final Class<R> suppliedClass;
    private final List<Parameter> parameterList = new ArrayList<>();
    private Function<BuildContext, R> functionThatBuildsTheInstance = $ -> null;
    private Map<String, Function<BuildContext, ?>> instantiationParameterSupplierOverrides = new HashMap<>();
    private Map<String, Function<BuildContext, ?>> setterParameters = new HashMap<>();


    public DynamicSupplierBuilder(Class<R> suppliedClass) throws IntrospectionException {
        this.suppliedClass = suppliedClass;

        stream(suppliedClass.getConstructors())
                .filter($ -> Modifier.isPublic($.getModifiers()))
                .min((o1, o2) -> -(o1.getParameterCount() - o2.getParameterCount()))
                .ifPresent(constructor -> useConstructor((Constructor<R>) constructor));

        BeanInfo beanInfo = Introspector.getBeanInfo(suppliedClass);
        stream(beanInfo.getPropertyDescriptors()).map(Object::toString).collect(Collectors.joining(",\n"));
    }

    @Override
    public StreamableSupplier<R> build() {
        BuildContext normalBuildContext = null;
        return () -> functionThatBuildsTheInstance.apply(normalBuildContext);
    }

    /**
     * Use this method to select a particular constructor by using the constructor to call it. The parameters of the
     * constructor must be defined using the $ methods that allow this class to identify them. The parameters may not
     * need to be explicitly named as they will be automatically derived from the parameter names of the constructor.
     * If they are not explicitly named it will only be possible to re-configure their Suppliers using the getter method
     * Lambda syntax if the constructor parameter names correspond exactly with the property names of the getters as defined in the
     * JavaBean specification. Otherwise you will have to re-configure their suppliers using their property names.
     *
     * @param constructorToBeMapped a Supplier defining the constructor to be mapped.
     */
    public DynamicSupplierBuilder<R> useConstructor(Function<BuildContext, R> constructorToBeMapped) {
        IntrospectionBuildContext introspectionBuildContext = new IntrospectionBuildContext();
        constructorToBeMapped.apply(introspectionBuildContext);
        this.parameterList.clear();
        this.parameterList.addAll(introspectionBuildContext.getParameterList());
        if (parameterList.stream().anyMatch($ -> $.getMappedName() == null)) { //try to derive the underlying parameter name from a matching constructor.
            try {
                Class[] constructorParameterClasses = parameterList.stream().map(Parameter::getParameterClass).toArray(Class[]::new);
                Constructor<R> constructor = suppliedClass.getConstructor(constructorParameterClasses);
                java.lang.reflect.Parameter[] parameters = constructor.getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    parameterList.get(i).setParameterName(parameters[i].getName());
                }
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("No constructor identifiable from the parameters called on the supplier and not all the property names are explicitly defined ", e);
            }
        }
        this.functionThatBuildsTheInstance = constructorToBeMapped;
        return this;
    }

    private void useConstructor(Constructor<R> constructor) {
        MethodType constructorMethodType = MethodType.methodType(void.class, constructor.getParameterTypes());
        try {
            MethodHandle constructorMethodHandle = MethodHandles.publicLookup().findConstructor(constructor.getDeclaringClass(), constructorMethodType);
            useFunction($ -> {
                try {
                    return (R) constructorMethodHandle.invokeWithArguments(stream(constructor.getParameters()).map(parameter -> $.p(parameter.getName(), parameter.getClass())).toArray());
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
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
     * @return
     */
    public DynamicSupplierBuilder<R> useFunction(Function<BuildContext, R> functionToBeUsed) {
        IntrospectionBuildContext introspectionBuildContext = new IntrospectionBuildContext();
        functionToBeUsed.apply(introspectionBuildContext);
        this.parameterList.clear();
        this.parameterList.addAll(introspectionBuildContext.getParameterList());
        this.functionThatBuildsTheInstance = functionToBeUsed;
        return this;
    }

    public static void main(String[] args) throws IntrospectionException {
        DynamicSupplierBuilder<TestClass> supplierParameterMap =
                new DynamicSupplierBuilder<>(TestClass.class)
                        .useConstructor($ -> new TestClass($.p("One"), $.p(Integer.class)))/*.for(TestClass::getValueOne).use(arbitraryString().ofLength....*/;
    }

    public static class TestClass {
        private final String valueOne;

        private final Integer valueTwo;


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
         * Use this parameter definition when you want to supply a default value that can be overridden.
         *
         * @param fixedParameter a fixed value that can be overridden.
         * @param <P>            The type of the parameter.
         * @return the fixed value.
         */
        <P> P p(P fixedParameter);

        /**
         * Use this parameter definition when you want to supply a default value that can be overridden and
         * you want to override the name of the parameter to make auto-mapping work or your code more legible.
         *
         * @param name           an override to the default name of the parameter to the constructor to help with the mapping.
         * @param fixedParameter a fixed value that can be overridden.
         * @param <P>            The type of the parameter.
         * @return the fixed value.
         */
        <P> P p(String name, P fixedParameter);
    }

    /**
     * This execution context is used when the build constructor or function is being introspected and collects details of the methods / parameters that are called on the object.
     */
    public class IntrospectionBuildContext implements BuildContext {

        public List<Parameter> getParameterList() {
            return parameterList;
        }

        private final List<Parameter> parameterList = new ArrayList<>();

        public <P> P p(Class<P> parameterClass) {
            return addParameterToConstructorList(new Parameter<>(parameterClass));
        }

        public <P> P p(String name, Class<P> parameterClass) {
            return addParameterToConstructorList(new Parameter<>(name, parameterClass));
        }

        public <P> P p(P fixedParameter) {
            return addParameterToConstructorList(new Parameter<>(fixedParameter));
        }

        public <P> P p(String name, P fixedParameter) {
            return addParameterToConstructorList(new Parameter<>(name, fixedParameter));
        }

        private <P> P addParameterToConstructorList(Parameter<P> parameter) {
            parameterList.add(parameter);
            return parameter.parameterSupplier.get();
        }
    }

    public class RuntimeBuildContext implements BuildContext {

        @Override
        public <P> P p(Class<P> parameterClass) {
            return null;
        }

        @Override
        public <P> P p(String name, Class<P> parameterClass) {
            return null;
        }

        @Override
        public <P> P p(P fixedParameter) {
            return null;
        }

        @Override
        public <P> P p(String name, P fixedParameter) {
            return null;
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

        @SuppressWarnings("unchecked")
        public Parameter(String nameOverride, P defaultParameterValue) {
            this.nameOverride = nameOverride;
            this.parameterClass = (Class<P>) defaultParameterValue.getClass();
            this.parameterSupplier = () -> defaultParameterValue;
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
