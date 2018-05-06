package com.robertboothby.djenni;

import com.robertboothby.djenni.core.StreamableSupplier;

/**
 * A builder of object Suppliers, implementing the &apos;Builder&apos; pattern. Instances of this interface will usually
 * be not thread safe and highly mutable.
 * @author robertboothby
 * @param <T>
 */
public interface SupplierBuilder<T> {

    /**
     * Build an instance of the generator based on the current configuration state.
     * @return an instance of the generator.
     */
    public StreamableSupplier<T> build();
}
