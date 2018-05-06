package com.robertboothby.djenni.core;

import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Extension interface to the basic {@link Supplier} interface that adds a stream method.
 * @param <T> The type of values supplied by the Supplier and any streams derived from it.
 */
public interface StreamableSupplier<T> extends Supplier<T> {

    /**
     * Return a Stream using this supplier as a source.
     * @param numberOfValues The number of values from this supplier to stream.
     * @return A Stream instance derived from this supplier.
     */
    default Stream<T> stream(int numberOfValues){
        return SupplierHelper.stream(this, numberOfValues);
    }
}
