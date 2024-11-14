package com.robertboothby.djenni;

import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.util.Configurable;

import java.util.function.Consumer;

/**
 * Multiple Inheritance interface that combines the SupplierBuilder and Configurable interfaces.
 * @param <T> The type of object that will eventually be supplied.
 * @param <U> A self reference to the type of the concrete implementation.
 */
public interface ConfigurableSupplierBuilder <T, U extends ConfigurableSupplierBuilder<T,U>>
        extends SupplierBuilder<T>, Configurable<U>{

    /**
     * Convenience method for when you want to get a supplier immediately from a configuration with no later customisation.
     * @param configuration The configuration to use.
     * @return A Supplier build from the configuration.
     */
    default StreamableSupplier<T> build(Consumer<U> configuration){
        return this.configure(configuration).build();
    }
}
