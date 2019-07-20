package com.robertboothby.djenni.util;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.sugar.And;
import com.robertboothby.djenni.sugar.Range;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.robertboothby.djenni.core.SupplierHelper.stream;
import static com.robertboothby.djenni.lang.IntegerSupplierBuilder.integerSupplier;
import static java.util.stream.Collectors.toList;

/**
 * Build a simple list of values from a supplier of entries and a supplier of integers that determines the number of
 * entries in the list. This is a foundational class for the supply of collections.
 * @param <T> The type of entries that will be held by the list.
 */
public class SimpleListSupplierBuilder<T> implements ConfigurableSupplierBuilder<List<T>, SimpleListSupplierBuilder<T>> {

    //TODO consider moving over to an integer supplier which will make it possible
    //to use the builder to trivially create empty lists.
    private Range<SimpleListSupplierBuilder<T>, Integer> range = Range.inclusive(this);
    private Supplier<? extends T> entries;

    @Override
    public StreamableSupplier<List<T>> build() {
        StreamableSupplier<Integer> sizes = integerSupplier(
                builder -> builder
                        .between(range.getMinimum())
                        .and(range.getMaximum() + 1)
        );

        return () -> {
            Integer numberOfValues = sizes.get();
            return stream(entries, numberOfValues).collect(toList());
        };
    }

    /**
     * Provide a Supplier for the entries to be created in the list.
     * @param entries The supplier for the entries
     * @return The builder for further configuration.
     */
    public SimpleListSupplierBuilder<T> entries(Supplier<? extends T> entries) {
        this.entries = entries;
        return this;
    }

    /**
     * Provide a SupplierBuilder for the entries to be created in the list. {@link SupplierBuilder#build()}
     * will be called immediately to derive the Supplier..
     * @param entriesBuilder The builder for the entries Supplier
     * @return The builder for further configuration.
     */
    public SimpleListSupplierBuilder<T> entries(SupplierBuilder<? extends T> entriesBuilder) {
        return this.entries(entriesBuilder.build());
    }

    /**
     * Convenience method that creates the Supplier from the a passed in configuration Consumer.
     * @param configuration The builder configuration to use to create the supplier.
     * @param <T> The type of entry within the list that will be supplied.
     * @return the configured Supplier.
     */
    public static <T> StreamableSupplier<List<T>> simpleList(Consumer<SimpleListSupplierBuilder<T>> configuration) {
        return SimpleListSupplierBuilder.<T>simpleList().build(configuration);
    }

    /**
     * Convenience method that creates an unconfigured SupplierBuilder.
     * @param <T> The type of entry that will be created.
     * @return The unconfigured SimpleListSupplierBuilder.
     */
    public static <T> SimpleListSupplierBuilder<T> simpleList() {
        return new SimpleListSupplierBuilder<>();
    }

    /**
     * Define a range of sizes (inclusive) for the lists that will be supplied.
     * @param value The lower bound of the size for the list.
     * @return an object that will allow you to set the upper bound before returning the SupplierBuilder
     *         for further configuration.
     */
    public And<SimpleListSupplierBuilder<T>, Integer> withSizeBetween(int value) {
        return range.between(value);
    }

    /**
     * Define a fixed size for the lists that will be supplied.
     * @param size The fixed size for the lists
     * @return the SupplierBuilder for further configuration.
     */
    public SimpleListSupplierBuilder<T> size(int size){
        return range.between(size).and(size);
    }
 }
