package com.robertboothby.djenni;

import com.robertboothby.djenni.core.StreamableSupplier;

import java.util.function.Consumer;

/**
 * A builder of object Suppliers, implementing the 'Builder' pattern. Instances of this interface will usually
 * be not thread safe and highly mutable.
 * @author robertboothby
 * @param <T> The type of the value that will be supplied.
 */
public interface SupplierBuilder<T> {

    /**
     * Build an instance of the generator based on the current configuration state.
     * @return an instance of the generator.
     */
    StreamableSupplier<T> build();
}
