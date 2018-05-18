package com.robertboothby.djenni.core;

import java.util.function.Supplier;

public class ThreadLocalSupplier<T> implements StreamableSupplier<T> {

    private final ThreadLocal<Supplier<T>> threadLocal;

    public ThreadLocalSupplier(Supplier<Supplier<T>> supplierSupplier) {
        this.threadLocal = ThreadLocal.withInitial(supplierSupplier);
    }

    @Override
    public T get() {
        return threadLocal.get().get();
    }

    public Supplier<T> getSupplier() {
        return threadLocal.get();
    }
}
