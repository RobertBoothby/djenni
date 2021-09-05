package com.robertboothby.djenni.dynamic.parameters;

import java.util.function.Supplier;

/**
 * This defines the contract of a BuildContext that is used to configure the parameters of a DynamicSupplierBuilder.
 */
public interface ParameterContext {

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
