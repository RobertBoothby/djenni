package com.robertboothby.djenni.util;

import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toMap;

/**
 * This SupplierBuilder creates a supplier of {@link java.util.Map}s. This can be used standalone or with the
 * {@link }MapSupplierHelper} and {@link MapTypes} to create a specific Map implementation.
 * @param <K> The type of the keys,
 * @param <V> The type of the values.
 */
public class SimpleMapSupplierBuilder<K,V> implements SupplierBuilder<Map<? extends K, ? extends V>> {

    private StreamableSupplier<? extends Map.Entry<? extends K, ? extends V>> entrySupplier;
    private Supplier<Integer> numberOfEntries;

    @Override
    public StreamableSupplier<Map<? extends K, ? extends V>> build() {
        return () -> entrySupplier.stream(numberOfEntries.get()).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public SimpleMapSupplierBuilder<K, V> withEntries(StreamableSupplier<? extends Map.Entry<? extends K, ? extends V>> entrySupplier) {
        this.entrySupplier = entrySupplier;
        return this;
    }

    public SimpleMapSupplierBuilder<K, V> withNumberOfEntries(Supplier<Integer> numberOfEntries) {
        this.numberOfEntries = numberOfEntries;
        return this;
    }

    public static <K, V> SimpleMapSupplierBuilder<K,V> map(){
        return new SimpleMapSupplierBuilder<>();
    }

    public static <K,V> Supplier<Map<? extends K, ? extends V>> map(Consumer<SimpleMapSupplierBuilder<K,V>> configuration){
        SimpleMapSupplierBuilder<K,V> builder  = map();
        configuration.accept(map());
        return builder.build();
    }
}
