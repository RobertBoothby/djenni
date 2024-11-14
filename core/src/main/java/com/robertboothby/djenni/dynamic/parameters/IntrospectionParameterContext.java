package com.robertboothby.djenni.dynamic.parameters;

import com.robertboothby.djenni.core.StreamableSupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * This execution context is used when the build constructor or function is being introspected and collects details of the methods / parameters that are called on the object.
 */
public class IntrospectionParameterContext implements ParameterContext {

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
    public <P> P p(StreamableSupplier<P> defaultSupplier) {
        return addParameterToList(new Parameter<>(defaultSupplier));
    }

    @Override
    public <P> P p(String name, StreamableSupplier<P> defaultSupplier) {
        return addParameterToList(new Parameter<>(name, defaultSupplier));
    }

    private <P> P addParameterToList(Parameter<P> parameter) {
        parameterList.add(parameter);
        return parameter.getParameterSupplier().get();
    }
}
