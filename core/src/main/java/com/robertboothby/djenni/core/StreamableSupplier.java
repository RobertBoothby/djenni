package com.robertboothby.djenni.core;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Extension interface to the basic {@link Supplier} interface that adds a stream method.
 * @param <T> The type of values supplied by the Supplier and any streams derived from it.
 */
public interface StreamableSupplier<T> extends Supplier<T> {

    /**
     * Return a limited Stream using this supplier as a source.
     * @param numberOfValues The number of values from this supplier to stream.
     * @return A Stream instance derived from this supplier.
     */
    default Stream<T> stream(long numberOfValues){
        return SupplierHelper.stream(this, numberOfValues);
    }

    /**
     * Return an infinite Stream using this supplier as a source.
     * @return A Stream instance derived from this supplier.
     */
    default Stream<T> stream() {
        return SupplierHelper.stream(this);
    }

    /**
     * Return a new Supplier derived from this supplier using the function passed in.
     * @param derivation The derivation function.
     * @param <R> The type returned from the derivation function.
     * @return A new Supplier derived from this one.
     */
    default <R> StreamableSupplier<R> derive(Function<T, R> derivation){
        return SupplierHelper.derived(derivation, this);
    }

    /**
     * Return a new Supplier derived from this supplier using the function passed in and a second supplier.
     * @param derivation The derivation function.
     * @param otherSupplier The other supplier.
     * @param <R> The type returned from the derivation function.
     * @return A new Supplier derived from this one and the other.
     */
    default <U,R> StreamableSupplier<R> derive(BiFunction<T, U ,R> derivation, Supplier<U> otherSupplier){
        return SupplierHelper.derived(derivation, this, otherSupplier);
    }
}
