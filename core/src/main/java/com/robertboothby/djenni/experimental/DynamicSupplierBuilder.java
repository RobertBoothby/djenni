package com.robertboothby.djenni.experimental;

import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * This is a dynamic supplier builder that will use reasonable defaults based on the JavaBeans method and constructor parameter naming conventions, but can be massively extended.
 * @param <T>
 */
public class DynamicSupplierBuilder<T> implements SupplierBuilder<T> {

    private final Class<T> suppliedClass;
    private final Map<String, Supplier> getters = new HashMap<>();

    public DynamicSupplierBuilder(Class<T> suppliedClass) {
        this.suppliedClass = suppliedClass;
    }

    @Override
    public StreamableSupplier<T> build() {
        return null;
    }

    public static <T> StreamableSupplier<T> supplierOf(Class<T> suppliedClass, SupplierParameterMap<T> supplierParameterMap){
        return new DynamicSupplierBuilder<T>(suppliedClass).build();
    }

    public <G> For<T,G> willSupply(Supplier<T> supplier){
        return $ -> {
            //TODO verify the method is one of the ones declared on the supplied class!
            getters.put($.getMethodName(), supplier);
            return this;
        };
    }

    public DynamicSupplierBuilder<T> constructorSupplier(SupplierParameterMap<T> supplierParameterMap){
        //Do something with the constructor parameter mapping.
        return this;
    }

    public interface For<T, G>{
        DynamicSupplierBuilder<T> forGetter(IntrospectableSupplier<G> getter);
    }

    public static void main(String[] args) {
        SupplierParameterMap<TestClass> supplierParameterMap = new SupplierParameterMap<>() {{
            mapSupplier(() -> new TestClass($("One"), $(Integer.class)));
        }};
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

    public static abstract class SupplierParameterMap<T> {

        private final List<Parameter> parameterList = new ArrayList<>();
        private Supplier<T> mappedSupplier;

        protected void mapSupplier(Supplier<T> supplierToBeMapped) {
            supplierToBeMapped.get(); //First pass to get the parameter list...
            //Assuming a constructor as a first pass, look for mathing constructor.

            this.mappedSupplier = supplierToBeMapped;
        }

        public <P> P $(Class<P> parameterClass){
            addParameterToConstructorList(new Parameter<>(parameterClass));
            return null;
        }

        /**
         * Use this parameter definition when there is no bad side effect to having a null value for the parameter and
         * you want to override the name of the parameter to make auto-mapping easier oryour code more legible.
         * @param name an override to the default name of the parameter to the constructor to help with the mapping.
         * @param parameterClass The class of the parameter.
         * @param <P> The type of the parameter
         * @return a null value.
         */
        public <P> P $(String name, Class<P> parameterClass){
            addParameterToConstructorList(new Parameter<>(name, parameterClass));
            return null;
        }

        /**
         * Use this parameter definition when you want to supply a default value that can be overridden.
         * @param fixedParameter a fixed value that can be overridden.
         * @param <P> The type of the parameter.
         * @return the fixed value.
         */
        public <P> P $(P fixedParameter){
            addParameterToConstructorList(new Parameter<>(fixedParameter));
            return fixedParameter;
        }

        /**
         * Use this parameter definition when you want to supply a default value that can be overridden and
         * you want to override the name of the parameter to make auto-mapping easier oryour code more legible.
         * @param name an override to the default name of the parameter to the constructor to help with the mapping.
         * @param fixedParameter a fixed value that can be overridden.
         * @param <P> The type of the parameter.
         * @return the fixed value.
         */
        public <P> P $(String name, P fixedParameter){
            addParameterToConstructorList(new Parameter<>(name, fixedParameter));
            return fixedParameter;
        }

        /**
         * Use this parameter definition when you want to use a supplier that can later be overridden. Be aware that
         * as part of initial configuration, it will use a value from the passed in supplier; if you want to prevent this
         * then use one of the other parameter methods and separately override the supplier.
         * @param parameterSupplier the supplier of the parameter values.
         * @param <P> The type of the parameter.
         * @return a value retrieved from the supplier.
         */
        public <P> P $(IntrospectableSupplier<P> parameterSupplier){
            addParameterToConstructorList(new Parameter<>(parameterSupplier));
            return parameterSupplier.get();
        }

        /**
         * Use this parameter definition when you want to use a supplier that can later be overridden and you want to
         * override the name of the parameter to make auto-mapping easier oryour code more legible. Be aware that as
         * part of initial configuration, it will use a value from the passed in supplier; if you want to prevent this
         * then use one of the other parameter methods and separately override the supplier.
         * @param name an override to the default name of the parameter to the constructor to help with the mapping.
         * @param parameterSupplier the supplier of the parameter values.
         * @param <P> The type of the parameter.
         * @return a value retrieved from the supplier.
         */
        public <P> P $(String name, IntrospectableSupplier<P> parameterSupplier){
            addParameterToConstructorList(new Parameter<>(name, parameterSupplier));
            return parameterSupplier.get();
        }

        void addParameterToConstructorList(Parameter parameter){
            parameterList.add(parameter);
        }
    }

    public static class Parameter<P> {
        private String name;
        private IntrospectableSupplier<P> parameterSupplier = () -> null;
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

        public Parameter(String nameOverride, P defaultParameterValue) {
            this.nameOverride = nameOverride;
            this.parameterClass = (Class<P>) defaultParameterValue.getClass();
            this.parameterSupplier = () -> defaultParameterValue;
        }
    }

}
