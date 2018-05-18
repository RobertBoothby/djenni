package com.robertboothby.djenni.core;

import com.robertboothby.djenni.SupplierBuilder;

import java.util.function.Supplier;

import static com.robertboothby.djenni.core.SupplierHelper.peek;

/**
 * This supplier is not thread safe and is best used in a single threaded context typically using some form of ThreadLocal.
 */
public class LastValueSupplier<T> implements StreamableSupplier<T> {

    private T lastValue;
    private final StreamableSupplier<T> source;

    public LastValueSupplier(Supplier<T> source) {
        this.source = peek(source, this::setLastValue);
    }

    @Override
    public T get() {
        return lastValue;
    }

    public StreamableSupplier<T> getSource() {
        return source;
    }

    public void setLastValue(T value){
        lastValue = value;
    }

    public static <T> LastValueSupplier<T> lastValueSupplier(Supplier<T> nakedSource){
        return new LastValueSupplier<>(nakedSource);
    }

    public static <T> LastValueSupplier<T> lastValueSupplier(SupplierBuilder<T> sourceBuilder){
        return new LastValueSupplier<>(sourceBuilder.build());
    }

}
