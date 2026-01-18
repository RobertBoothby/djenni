package com.robertboothby.djenni.dynamic;

import com.robertboothby.djenni.core.StreamableSupplier;

/**
 * Sentinel supplier used to indicate that the bean's own default value should be preserved for a property.
 */
public final class UseDefaultValueSupplier<T> implements StreamableSupplier<T> {

    private static final UseDefaultValueSupplier<?> INSTANCE = new UseDefaultValueSupplier<>();

    private UseDefaultValueSupplier() {
    }

    @SuppressWarnings("unchecked")
    public static <T> UseDefaultValueSupplier<T> useDefaultValueSupplier() {
        return (UseDefaultValueSupplier<T>) INSTANCE;
    }

    @Override
    public T get() {
        return null;
    }
}
