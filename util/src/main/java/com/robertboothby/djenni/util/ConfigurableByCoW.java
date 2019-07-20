package com.robertboothby.djenni.util;

import java.util.function.Function;

/**
 * <p>
 *     Copy on Write (CoW) configuration interface equivalent to Configurable. Allows us to work with immutable types in a
 *     fluent manner.
 * </p>
 * <p>
 *     This is primarily intended to allow a fluent composable syntax for applying configurations.
 * </p>
 *
 * @param <T> The immutable type that is being configured.
 */
public interface ConfigurableByCoW<T extends ConfigurableByCoW<T>> {

    /**
     * Create a new copy of this configurable, immutable object by applying the Function to the current object.
     * @param config The configuration to apply.
     * @return the new
     */
    @SuppressWarnings("unchecked")
    default T configure(Function<T, T> config){
        return config.apply((T) this);
    }
}
