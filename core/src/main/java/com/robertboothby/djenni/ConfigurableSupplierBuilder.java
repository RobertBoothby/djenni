package com.robertboothby.djenni;

import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.util.Configurable;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Multiple Inheritance interface that combines the SupplierBuilder and Configurable interfaces.
 * @param <T> The type of object that will eventually be supplied.
 * @param <U> A self reference to the type of the concrete implementation.
 */
public interface ConfigurableSupplierBuilder <T, U extends ConfigurableSupplierBuilder<T,U>>
        extends SupplierBuilder<T>, Configurable<U>{

    default StreamableSupplier<T> build(Consumer<U> configuration){
        return this.configure(configuration).build();
    }
}
