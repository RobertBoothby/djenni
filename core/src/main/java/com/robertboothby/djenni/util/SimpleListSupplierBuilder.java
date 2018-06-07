package com.robertboothby.djenni.util;

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

public class SimpleListSupplierBuilder<T> implements SupplierBuilder<List<T>> {

    private Range<SimpleListSupplierBuilder<T>, Integer> range = Range.inclusive(this);
    private Supplier<? extends T> entries;

    @Override
    public StreamableSupplier<List<T>> build() {
        StreamableSupplier<Integer> sizes = integerSupplier(
                builder -> builder
                        .between(range.getMinimum())
                        .and(range.getMaximum())
        );

        return () -> {
            Integer numberOfValues = sizes.get();
            return stream(entries, numberOfValues).collect(toList());
        };
    }

    public SimpleListSupplierBuilder<T> entries(Supplier<? extends T> entries) {
        this.entries = entries;
        return this;
    }

    public SimpleListSupplierBuilder<T> entries(SupplierBuilder<? extends T> entriesBuilder) {
        this.entries = entriesBuilder.build();
        return this;
    }

    public static <T> StreamableSupplier<List<T>> simpleList(Consumer<SimpleListSupplierBuilder<T>> configuration) {
        SimpleListSupplierBuilder<T> builder = simpleList();
        configuration.accept(builder);
        return builder.build();
    }

    public static <T> SimpleListSupplierBuilder<T> simpleList() {
        return new SimpleListSupplierBuilder<>();
    }

    public And<SimpleListSupplierBuilder<T>, Integer> withSizeBetween(int value) {
        return range.between(value);
    }

    public SimpleListSupplierBuilder<T> size(int size){
        return range.between(size).and(size);
    }
 }
