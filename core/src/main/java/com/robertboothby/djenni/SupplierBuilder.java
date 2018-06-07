package com.robertboothby.djenni;

import com.robertboothby.djenni.core.StreamableSupplier;

import java.util.function.Consumer;

/**
 * A builder of object Suppliers, implementing the &apos;Builder&apos; pattern. Instances of this interface will usually
 * be not thread safe and highly mutable.
 * @author robertboothby
 * @param <T>
 */
public interface SupplierBuilder<T> {

    /**
     * Build an instance of the generator based on the current configuration state.
     * @return an instance of the generator.
     */
    StreamableSupplier<T> build();

    /**
     * Utility method to make it easier to build a supplier from a supplier builder with a configuration consumer.
     * @param supplierBuilder The supplier builder to configure and use.
     * @param configuration The configuration to use.
     * @param <T> The type of values to be supplied by the supplier being created.
     * @param <U> The type of the SupplierBuilder.
     * @return A configured supplier based on the previous configuration of the supplier builder and the newly applied configuration.
     */
    static <T, U extends SupplierBuilder<T>> StreamableSupplier<T> buildConfig(U supplierBuilder, Consumer<U> configuration){
        configuration.accept(supplierBuilder);
        return supplierBuilder.build();
    }

}
