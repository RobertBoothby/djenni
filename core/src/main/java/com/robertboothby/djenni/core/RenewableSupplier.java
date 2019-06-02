package com.robertboothby.djenni.core;

import java.util.function.Supplier;

/**
 * This allows you to "renew" an underlying Supplier on demand. This is accomplished by calling get() on a Supplier of
 * Suppliers.
 * @param <T>
 */
public class RenewableSupplier<T> implements StreamableSupplier<T> {

    private final Supplier<Supplier<T>> baseSupplier;
    private Supplier<T> currentSupplier;

    public RenewableSupplier(Supplier<Supplier<T>> baseSupplier) {
        this.baseSupplier = baseSupplier;
        renew();
    }

    @Override
    public T get() {
        return currentSupplier.get();
    }

    public void renew(){
        this.currentSupplier = baseSupplier.get();
    }
}