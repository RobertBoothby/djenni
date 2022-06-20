package com.robertboothby.djenni.core;

import java.util.function.Supplier;

import static com.robertboothby.djenni.core.SupplierHelper.afterGetCalled;

/**
 * <p>
 *     A Supplier that wraps another supplier returning the latest value that the CachingSupplier has retrieved from
 *     the underlying supplier from the {@link #get()}  method until {@link #next()} is called.
 * </p>
 */
public class CachingSupplier<T> implements StreamableSupplier<T> {

    private final ThreadLocal<T> lastValue = new ThreadLocal<>();

    private final StreamableSupplier<T> source;

    /**
     * Construct an instance of caching supplier using the passed in source supplier
     * @param source the source supplier.
     */
    public CachingSupplier(Supplier<T> source) {
        this.source = afterGetCalled(source, this::setLastValue);
        next();
    }

    @Override
    public T get() {
        return lastValue.get();
    }

    /**
     * Get the next value from the wrapped supplier and cache it.
     */
    public T next() {
        return source.get();
    }

    private void setLastValue(T value){
        lastValue.set(value);
    }

    /**
     * Wrap a source supplier in a caching supplier.
     * @param source The source to be wrapped.
     * @return The source wrapped in a caching supplier
     * @param <T> The type of values returned by the source supplier and the newly created caching supplier.
     */
    public static <T> CachingSupplier<T> cacheSuppliedValues(Supplier<T> source){
        return new CachingSupplier<>(source);
    }
}
