package com.robertboothby.djenni.core;

import java.util.function.Supplier;

import static com.robertboothby.djenni.core.SupplierHelper.peek;

/**
 * <p>
 *     A Supplier that can spawn linked Suppliers that will supply exactly the same value as the last get() did on this
 *     supplier. This is useful in richer domain models where you may need what are effectively 'Foreign Key' references
 *     or event duplicate references. However do be careful as ordering matters.
 * </p>
 * <p>
 *     This supplier is thread safe in that its linked suppliers will return the same value as the last get() called on
 *     the same thread - however this means that you cannot share values between threads using this implementation.
 * </p>
 */
public class LinkableSupplier<T> implements StreamableSupplier<T> {

    private final ThreadLocal<T> lastValue = new ThreadLocal<>();

    private final StreamableSupplier<T> source;

    public LinkableSupplier(Supplier<T> source) {
        this.source = peek(source, this::setLastValue);
    }

    @Override
    public T get() {
        return source.get();
    }

    public StreamableSupplier<T> linked(){
        return lastValue::get;
    }

    private void setLastValue(T value){
        lastValue.set(value);
    }

    public static <T> LinkableSupplier<T> linkable(Supplier<T> nakedSource){
        return new LinkableSupplier<>(nakedSource);
    }
}
