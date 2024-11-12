package com.robertboothby.djenni.util;

import java.util.function.Function;

/**
 * <p>
 *     Copy on Write (CoW) configuration interface equivalent to Configurable. Allows us to work with immutable types in a
 *     fluent manner particularly when using a predefined default configuration that you may want to override or extend further.
 * </p>
 * @param <T> The immutable type that is being configured.
 */
public interface CopyOnWriteConfigurable<T extends CopyOnWriteConfigurable<T>> {

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
