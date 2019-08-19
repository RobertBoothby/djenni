package com.robertboothby.djenni.util;

import java.util.function.Consumer;

/**
 * <p>
 *     Useful interface that allows us to trivially add a method that allows a Consumer to configure the class in question.
 * </p>
 * <p>
 *     This is primarily intended to allow us to compose multiple pre-canned configurations together in a fluent style.
 * </p>
 * @param <T> The concrete implementation of the Configurable interface.
 */
public interface Configurable<T extends Configurable<T>> {

    /**
     * Supply a Consumer that will configure this class.
     * @param config The configuration to use.
     * @return This class for further actions.
     */
    @SuppressWarnings("unchecked")
    default T configure(Consumer<T> config){
        config.accept((T)this);
        return (T)this;
    }
}
