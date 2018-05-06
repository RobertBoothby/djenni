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
    private Supplier<T> entryGenerator;

    @Override
    public StreamableSupplier<List<T>> build() {
        Supplier<Integer> sizeSupplier = integerSupplier(
                builder -> builder
                        .between(range.getMinimum())
                        .and(range.getMaximum())
        );

        return () -> stream(entryGenerator, sizeSupplier.get()).collect(toList());
    }

    public SimpleListSupplierBuilder<T> withEntrySupplier(Supplier<T> entryGenerator) {
        this.entryGenerator = entryGenerator;
        return this;
    }

    public SimpleListSupplierBuilder<T> withEntrySupplier(SupplierBuilder<T> builder) {
        this.entryGenerator = builder.build();
        return this;
    }

    public static <T> SimpleListSupplierBuilder<T> simpleList(Consumer<SimpleListSupplierBuilder<T>> consumer) {
        SimpleListSupplierBuilder<T> builder = new SimpleListSupplierBuilder<>();
        consumer.accept(builder);
        return builder;
    }

    public And<SimpleListSupplierBuilder<T>, Integer> withSizeBetween(int value) {
        return range.between(value);
    }
}
