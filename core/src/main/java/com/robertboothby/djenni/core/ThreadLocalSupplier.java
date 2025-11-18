package com.robertboothby.djenni.core;

import java.util.function.Supplier;

/**
 * Adapter that provides a separate delegate supplier per thread. The first time a thread calls {@link #get()} a new
 * inner supplier is obtained from the provided factory and cached in a {@link ThreadLocal}. This allows non-thread-safe
 * suppliers to be used safely in multi-threaded scenarios without additional synchronization.
 *
 * @param <T> value type supplied
 */
public class ThreadLocalSupplier<T> implements StreamableSupplier<T> {

    private final ThreadLocal<Supplier<T>> threadLocal;

    /**
     * Construct a thread-localizing supplier.
     * @param supplierSupplier factory used to create the per-thread supplier instances
     */
    public ThreadLocalSupplier(Supplier<Supplier<T>> supplierSupplier) {
        this.threadLocal = ThreadLocal.withInitial(supplierSupplier);
    }

    @Override
    public T get() {
        return threadLocal.get().get();
    }

    /**
     * Expose the lazily-created supplier bound to the current thread. This is mainly useful for testing or when the
     * caller needs to invoke methods specific to the underlying supplier implementation.
     */
    public Supplier<T> getSupplier() {
        return threadLocal.get();
    }
}
