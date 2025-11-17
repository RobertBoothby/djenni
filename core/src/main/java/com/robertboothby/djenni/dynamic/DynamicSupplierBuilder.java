package com.robertboothby.djenni.dynamic;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.core.SupplierHelper;
import com.robertboothby.djenni.dynamic.parameters.IntrospectionParameterContext;
import com.robertboothby.djenni.dynamic.parameters.Parameter;
import com.robertboothby.djenni.dynamic.parameters.ParameterContext;
import com.robertboothby.djenni.dynamic.parameters.RuntimeParameterContext;
import com.robertboothby.djenni.util.lambda.introspectable.IntrospectableBiConsumer;
import com.robertboothby.djenni.util.lambda.introspectable.IntrospectableFunction;

import java.beans.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toSet;

/**
 * This is a dynamic supplier builder that will use reasonable defaults based on the JavaBeans method and constructor parameter naming conventions, but can be massively extended.
 *
 * <em>TO MAKE THE AUTOMATED PARAMETER NAMING WORK, USE -parameters IN THE JAVA COMPILATION. IN INTELLIJ THIS NEEDS TO BE DONE AT THE TOP LEVEL OF THE PROJECT AS WELL AS THE SUB-MODULES!!</em>
 * TODO - don't forget that we may have novel parameters defined during the introspection process that are not defined in the constructor or setters. We need to add them to the parameter list if not already defined and execute them - Done but test this!!!!!.
 * TODO - don't forget bean setters - We need to add them to the default function if their properties are not already defined property the constructor and only call them if the relevant properties are actually populated - KInd of done but may be better via semi-dynamic decoration during instantiation - looking at the parameters and adding setter calls as needed.
 * DONE - don't forget that not all constructors / methods allow null values. During introspection we may need to allow for dummy values to be supplied. Completed property allowing sample / default values for parameters and suppliers.
 * DONE - don't forget primitive types - while we can get away often with supplying null values for object types we cannot do so for primitive types. We need to create a mechanism to provide a reasonable default.
 *
 * @param <C> The type of the Class that will be supplied in the Suppliers created by this builder.
 */
public class DynamicSupplierBuilder<C> implements ConfigurableSupplierBuilder<C, DynamicSupplierBuilder<C>> {

    private final Class<C> suppliedClass;
    private final List<Parameter<?>> parameterList = new ArrayList<>();
    private final BeanInfo beanInfo;
    private Function<ParameterContext, C> functionThatBuildsTheInstance = $ -> null;
    private Map<String, Function<ParameterContext, ?>> instantiationParameterSupplierOverrides = new HashMap<>();
    private Map<String, Function<ParameterContext, ?>> setterParameters = new HashMap<>();
    //Dummy object used for introspection;
    private C dummy;


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
    public DynamicSupplierBuilder(Class<C> suppliedClass) throws IntrospectionException {
        this.suppliedClass = suppliedClass;

        stream(suppliedClass.getConstructors())
                .filter($ -> Modifier.isPublic($.getModifiers()))
                .max(comparingInt(Constructor::getParameterCount)) // Get the constructor with the most parameters.
                .ifPresent(constructor -> useDefaultedConstructor((Constructor<C>) constructor));

        beanInfo = Introspector.getBeanInfo(suppliedClass);

        //Add all the setters to set of parameters.
        stream(beanInfo.getPropertyDescriptors())
                .filter($ -> $.getWriteMethod() != null)
                .filter($ -> !parameterList.stream().map(Parameter::getMappedName).collect(toSet()).contains($.getName()))
                .forEach($ -> {
                    parameterList.add(new Parameter<>($.getName(), (Class<Class<?>>) $.getPropertyType()));
                    Function<ParameterContext, C> oldFunction = functionThatBuildsTheInstance;
                    functionThatBuildsTheInstance = bc -> {
                        C result = oldFunction.apply(bc);
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
    public StreamableSupplier<C> build() {
        List<? extends Parameter<?>> parameterList = this.parameterList.stream().map(Parameter::new).collect(Collectors.toList());
        return () -> functionThatBuildsTheInstance.apply(new RuntimeParameterContext(parameterList));
    }

    /**
     * Use this method to select a particular constructor by using the constructor to call it. The parameters of the
     * constructor must be defined using the $ methods that allow this class to identify them. The parameters may not
     * need to be explicitly named as they will be automatically derived from the parameter names of the constructor
     *
     * @param constructorToBeMapped a Function defining the constructor to be mapped using the BuildContext to derive
     *                              the parameters.
     */
    public DynamicSupplierBuilder<C> useConstructor(Function<ParameterContext, C> constructorToBeMapped) {
        IntrospectionParameterContext introspectionBuildContext = new IntrospectionParameterContext();
        constructorToBeMapped.apply(introspectionBuildContext);
        this.parameterList.clear();
        this.parameterList.addAll(introspectionBuildContext.getParameterList());
        if (parameterList.stream().anyMatch($ -> $.getMappedName() == null)) { //try to derive the underlying parameter name from a matching constructor.
            try {
                Class<?>[] constructorParameterClasses = parameterList.stream().map(Parameter::getParameterClass).toArray(Class[]::new);
                Constructor<C> constructor = suppliedClass.getConstructor(constructorParameterClasses);
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
    private void useDefaultedConstructor(Constructor<C> constructor) {
        MethodType constructorMethodType = MethodType.methodType(void.class, constructor.getParameterTypes());
        try {
            MethodHandle constructorMethodHandle = MethodHandles.publicLookup().findConstructor(constructor.getDeclaringClass(), constructorMethodType);
            useFunction($ -> {
                try {
                    dummy = (C) constructorMethodHandle.invokeWithArguments(stream(constructor.getParameters()).map(parameter -> $.p(parameter.getName(), parameter.getType())).toArray());
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
    public DynamicSupplierBuilder<C> useFunction(Function<ParameterContext, C> functionToBeUsed) {
        IntrospectionParameterContext introspectionBuildContext = new IntrospectionParameterContext();
        functionToBeUsed.apply(introspectionBuildContext);
        this.parameterList.clear();
        this.parameterList.addAll(introspectionBuildContext.getParameterList());
        this.functionThatBuildsTheInstance = functionToBeUsed;
        return this;
    }

    public <P> DynamicSupplierBuilder<C> property(IntrospectableFunction<? super C, P> getter, Supplier<P> propertySupplier) {
        getter(getter).setParameterSupplier(propertySupplier);
        return this;
    }

    public <P> DynamicSupplierBuilder<C> property(IntrospectableFunction<? super C, P> getter, SupplierBuilder<P> propertySupplierBuilder) {
        getter(getter).setParameterSupplier(propertySupplierBuilder.build());
        return this;
    }

    public <P> DynamicSupplierBuilder<C> property(IntrospectableBiConsumer<? super C, P> setter, Supplier<P> propertySupplier) {
        setter(setter).setParameterSupplier(propertySupplier);
        return this;
    }

    public <P> DynamicSupplierBuilder<C> property(IntrospectableBiConsumer<? super C, P> setter, SupplierBuilder<P> propertySupplierBuilder) {
        setter(setter).setParameterSupplier(propertySupplierBuilder.build());
        return this;
    }

    @SuppressWarnings("unchecked")
    public <P> DynamicSupplierBuilder<C> property(String propertyName, Supplier<P> propertySupplier) {
        parameterList.stream().filter($ -> $.getMappedName().equals(propertyName)).findFirst().ifPresent($ -> {
            ((Parameter<P>)$).setParameterSupplier(propertySupplier);
        });
        return this;
    }

    public DynamicSupplierBuilder<C> property(String propertyName, SupplierBuilder<?> propertySupplierBuilder) {
        property(propertyName, propertySupplierBuilder.build());
        return this;
    }

    private <T> Parameter<T> getter(IntrospectableFunction<? super C, T> getter) {
        String propertyName = stream(beanInfo.getPropertyDescriptors())
                .filter($ -> $.getReadMethod() != null)
                .filter($ -> $.getReadMethod().getName().equals(getter.getMethodName()))
                .findFirst()
                .map(FeatureDescriptor::getName).orElseThrow();

        Optional<Parameter<?>> first = parameterList.stream().filter($ -> $.getMappedName().equals(propertyName)).findFirst();
        if(first.isEmpty()){
            Parameter<T> parameter = new Parameter<>(propertyName, (Class<T>) getter.getReturnType());
            parameterList.add(parameter);
            Method setterMethod = stream(beanInfo.getPropertyDescriptors())
                    .filter($ -> $.getWriteMethod() != null)
                    .filter($ -> $.getName().equals(propertyName))
                    .findFirst()
                    .map(PropertyDescriptor::getWriteMethod).orElseThrow(() -> new RuntimeException("No setter found for property " + propertyName));
            functionThatBuildsTheInstance = bc -> {
                C result = functionThatBuildsTheInstance.apply(bc);
                try {
                    setterMethod.invoke(result, bc.p(propertyName, (Class<T>) getter.getReturnType()));
                } catch (Exception e) {
                    if (e instanceof RuntimeException) {
                        throw (RuntimeException) e;
                    } else {
                        throw new RuntimeException(e);
                    }
                }
                return result;
            };

            return parameter;
        } else {
            return (Parameter<T>) first.get();
        }
    }

    private <T> Parameter<T> setter(IntrospectableBiConsumer<? super C, ? extends T> setter) {
        String propertyName = stream(beanInfo.getPropertyDescriptors())
                .filter($ -> $.getWriteMethod() != null)
                .filter($ -> $.getWriteMethod().getName().equals(setter.getMethodName()))
                .findFirst()
                .map(FeatureDescriptor::getName).orElseThrow();

        Optional<Parameter<?>> first = parameterList.stream().filter($ -> $.getMappedName().equals(propertyName)).findFirst();
        if(first.isEmpty()){
            Parameter<T> parameter = new Parameter<>(propertyName, (Class<T>) setter.getParameterType());
            parameterList.add(parameter);
            Method setterMethod = stream(beanInfo.getPropertyDescriptors())
                    .filter($ -> $.getWriteMethod() != null)
                    .filter($ -> $.getName().equals(propertyName))
                    .findFirst()
                    .map(PropertyDescriptor::getWriteMethod).orElseThrow(() -> new RuntimeException("No setter found for property " + propertyName));
            functionThatBuildsTheInstance = bc -> {
                C result = functionThatBuildsTheInstance.apply(bc);
                try {
                    setterMethod.invoke(result, bc.p(propertyName, (Class<T>) setter.getParameterType()));
                } catch (Exception e) {
                    if (e instanceof RuntimeException) {
                        throw (RuntimeException) e;
                    } else {
                        throw new RuntimeException(e);
                    }
                }
                return result;
            };
            return parameter;
        } else {
            return (Parameter<T>) first.get();
        }
    }

    public static <T> DynamicSupplierBuilder<T> supplierFor(Class<T> tClass) {
        try {
            return new DynamicSupplierBuilder<>(tClass);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    public <P> DynamicSupplierBuilder<C> clearProperty(IntrospectableBiConsumer<? super C, P> setter) {
        setter(setter).setParameterSupplier(SupplierHelper.nullSupplier());
        return this;
    }
}
