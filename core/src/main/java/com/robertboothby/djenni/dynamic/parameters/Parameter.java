package com.robertboothby.djenni.dynamic.parameters;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * This class encapsulates a parameter involved in the creation of a data class using the DynamicSupplierBuilder.
 * @param <P> The type of the parameter to be created.
 */
public class Parameter<P> {
    private String parameterName;
    private Supplier<P> parameterSupplier = () -> null;
    private String nameOverride;
    private Class<? extends P> parameterClass;

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
