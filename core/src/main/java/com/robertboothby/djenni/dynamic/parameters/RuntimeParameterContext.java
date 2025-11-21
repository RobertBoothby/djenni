package com.robertboothby.djenni.dynamic.parameters;

import com.robertboothby.djenni.core.StreamableSupplier;

import java.util.List;
import java.util.function.Supplier;

/**
 * This build context is used when we are actually supplying values, using the configured suppliers for the values
 * that have been defined.
 */
@SuppressWarnings("unchecked")
public class RuntimeParameterContext implements ParameterContext {

    private final List<? extends Parameter<?>> parameters;
    private int parameterPosition = 0;
    private boolean lastValueWasUseDefaultSupplier;

    public RuntimeParameterContext(List<? extends Parameter<?>> parameters) {
        this.parameters = parameters;
    }

    @Override
    public <P> P p(Class<P> parameterClass) {
        Parameter<?> parameter = parameters.get(parameterPosition++);
        lastValueWasUseDefaultSupplier = parameter.isUseDefaultValueSupplier();
        return (P) parameter.getParameterSupplier().get();
    }

    @Override
    public <P> P p(String name, Class<P> parameterClass) {
        Parameter<?> parameter = parameters.get(parameterPosition++);
        lastValueWasUseDefaultSupplier = parameter.isUseDefaultValueSupplier();
        return (P) parameter.getParameterSupplier().get();
    }

    @Override
    public <P> P p(P fixedParameter) {
        Parameter<?> parameter = parameters.get(parameterPosition++);
        lastValueWasUseDefaultSupplier = parameter.isUseDefaultValueSupplier();
        return (P) parameter.getParameterSupplier().get();
    }

    @Override
    public <P> P p(String name, P fixedParameter) {
        Parameter<?> parameter = parameters.get(parameterPosition++);
        lastValueWasUseDefaultSupplier = parameter.isUseDefaultValueSupplier();
        return (P) parameter.getParameterSupplier().get();
    }

    @Override
    public <P> P p(StreamableSupplier<P> defaultSupplier) {
        Parameter<?> parameter = parameters.get(parameterPosition++);
        lastValueWasUseDefaultSupplier = parameter.isUseDefaultValueSupplier();
        return (P) parameter.getParameterSupplier().get();
    }

    @Override
    public <P> P p(String name, StreamableSupplier<P> defaultSupplier) {
        Parameter<?> parameter = parameters.get(parameterPosition++);
        lastValueWasUseDefaultSupplier = parameter.isUseDefaultValueSupplier();
        return (P) parameter.getParameterSupplier().get();
    }

    public boolean lastValueWasUseDefaultValueSupplier() {
        return lastValueWasUseDefaultSupplier;
    }
}
