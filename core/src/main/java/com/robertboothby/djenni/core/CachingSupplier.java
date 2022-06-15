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

    public CachingSupplier(Supplier<T> source) {
        this.source = afterGetCalled(source, this::setLastValue);
        next();
    }

    @Override
    public T get() {
        return lastValue.get();
    }

    public void next() {
        source.get();
    }

    private void setLastValue(T value){
        lastValue.set(value);
    }

    public static <T> CachingSupplier<T> cacheSuppliedValues(Supplier<T> nakedSource){
        return new CachingSupplier<>(nakedSource);
    }
}
