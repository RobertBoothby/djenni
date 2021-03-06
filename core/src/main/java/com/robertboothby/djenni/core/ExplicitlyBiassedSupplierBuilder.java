package com.robertboothby.djenni.core;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * This builder allows for the configuration of ExplicitlyBiassedSupplier instances.
 */
public class ExplicitlyBiassedSupplierBuilder<T> implements ConfigurableSupplierBuilder<T, ExplicitlyBiassedSupplierBuilder<T>> {

    /**
     * A default weight of 1.0D.
     */
    public static final Double DEFAULT_WEIGHT = 1.0D;

    private List<ExplicitlyBiassedSupplier.BiasDetail<T>> biaslist = new ArrayList<>();

    /**
     * Add a supplier with the {@link #DEFAULT_WEIGHT}.
     * @param supplier The supplier to add.
     * @return the builder for further configuration.
     */
    public ExplicitlyBiassedSupplierBuilder addSupplier(Supplier<T> supplier) {
        return addSupplier(supplier, DEFAULT_WEIGHT);
    }

    /*
     * Add a supplier with the passed in weight.
     * @param supplier The supplier to add.
     * @param weight the weight of the supplier being added.
     * @return the builder for further configuration.
     * @param <U> the type of the supplier being added.
     */
    public <U extends Supplier<T>> ExplicitlyBiassedSupplierBuilder<T> addSupplier(U supplier, double weight) {
        biaslist.add(ExplicitlyBiassedSupplier.biasDetail(supplier, weight));
        return this;
    }

    /**
     * Add (or update) multiple values with the {@link #DEFAULT_WEIGHT}.
     * @param suppliers the values to add.
     * @return the builder for further configuration.
     */
    @SafeVarargs
    public final ExplicitlyBiassedSupplierBuilder<T> addSuppliers(Supplier<T> ... suppliers) {
        for (Supplier<T> supplier : suppliers){
            addSupplier(supplier);
        }
        return this;
    }

    /**
     * Add (or update) multiple values with the passed in weight.
     * @param suppliers the values to add.
     * @param weight the weight of the values being added.
     * @return the builder for further configuration.
     */
    @SafeVarargs
    public final ExplicitlyBiassedSupplierBuilder<T> addSuppliers(double weight, Supplier<T> ... suppliers) {
        for (Supplier<T> supplier : suppliers){
            addSupplier(supplier, weight);
        }
        return this;
    }

    @Override
    public ExplicitlyBiassedSupplier<T> build() {
        return new ExplicitlyBiassedSupplier<T>(new ArrayList<>(biaslist));
    }

    /**
     * Get a new instance of the builder to be configured.
     * @param <T> The type of object that will be generated by the configured suppliers.
     * @param classType The class representing the type of the value that will be supplied
     * @return a new instance of the builder.
     */
    public static <T> ExplicitlyBiassedSupplierBuilder<T> explicitlyBiassedSupplierFor(Class<T> classType) {
        return new ExplicitlyBiassedSupplierBuilder<T>();
    }

    /**
     * Get a new instance of the builder to be configured.
     * @param configuration A consumer that will configure the builder.
     * @param <T> The type of object that will be generated by the configured suppliers.
     * @return a configured supplier.
     */
    public static <T> ExplicitlyBiassedSupplier<T> explicitlyBiassedSupplierFor(Consumer<ExplicitlyBiassedSupplierBuilder<T>> configuration) {
        ExplicitlyBiassedSupplierBuilder<T> builder = new ExplicitlyBiassedSupplierBuilder<>();
        configuration.accept(builder);
        return builder.build();
    }
}
