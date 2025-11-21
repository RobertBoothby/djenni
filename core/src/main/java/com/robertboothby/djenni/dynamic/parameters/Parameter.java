package com.robertboothby.djenni.dynamic.parameters;

import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.dynamic.DefaultSuppliers;
import com.robertboothby.djenni.dynamic.UseDefaultValueSupplier;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * This class encapsulates a parameter involved in the creation of a data class using the DynamicSupplierBuilder.
 * @param <P> The type of the parameter to be created.
 */
public class Parameter<P> {
    private String parameterName;
    private Supplier<P> parameterSupplier;
    private Supplier<P> defaultParameterSupplier;
    private String nameOverride;
    private Class<? extends P> parameterClass;
    private boolean parameterSupplierExplicitlySet;

    public Parameter(Class<P> parameterClass) {
        this(null, parameterClass);
        this.parameterSupplier = DefaultSuppliers.defaultSuppliers().getSupplierForClass(parameterClass);
        this.defaultParameterSupplier = this.parameterSupplier;
    }

    public Parameter(String nameOverride, Class<P> parameterClass) {
        this.nameOverride = nameOverride;
        this.parameterClass = parameterClass;
        this.parameterSupplier = DefaultSuppliers.defaultSuppliers().getSupplierForClassAndProperty(parameterClass, nameOverride);
        this.defaultParameterSupplier = this.parameterSupplier;
    }

    public Parameter(P defaultParameterValue) {
        this(null, defaultParameterValue);
    }

    public Parameter(StreamableSupplier<P> defaultParameterSupplier) {
        this(null, defaultParameterSupplier);
    }

    @SuppressWarnings("unchecked")
    public Parameter(String nameOverride, P defaultParameterValue) {
        this.nameOverride = nameOverride;
        this.parameterClass = (Class<P>) defaultParameterValue.getClass();
        this.parameterSupplier = () -> defaultParameterValue;
        this.defaultParameterSupplier = this.parameterSupplier;
    }

    @SuppressWarnings("unchecked")
    public Parameter(String nameOverride, StreamableSupplier<P> defaultParameterSupplier) {
        this.nameOverride = nameOverride;
        this.parameterClass = (Class<P>) defaultParameterSupplier.get().getClass();
        this.parameterSupplier = defaultParameterSupplier;
        this.defaultParameterSupplier = this.parameterSupplier;
    }

    public Parameter(Parameter<P> original) {
        this.parameterName = original.parameterName;
        this.parameterSupplier = original.parameterSupplier;
        this.defaultParameterSupplier = original.defaultParameterSupplier;
        this.nameOverride = original.nameOverride;
        this.parameterClass = original.parameterClass;
        this.parameterSupplierExplicitlySet = original.parameterSupplierExplicitlySet;
    }


    public String getParameterName() {
        return parameterName;
    }

    public Supplier<P> getParameterSupplier() {
        return parameterSupplier;
    }

    public void setParameterSupplier(Supplier<P> parameterSupplier) {
        setParameterSupplier(parameterSupplier, true);
    }

    public void setParameterSupplier(Supplier<P> parameterSupplier, boolean explicitlySet) {
        this.parameterSupplier = parameterSupplier;
        this.parameterSupplierExplicitlySet = explicitlySet;
    }

    public void resetToDefaultSupplier() {
        setParameterSupplier(this.defaultParameterSupplier, false);
    }

    public boolean isParameterSupplierExplicitlySet() {
        return parameterSupplierExplicitlySet;
    }

    public boolean isUseDefaultValueSupplier() {
        return parameterSupplier instanceof UseDefaultValueSupplier;
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
