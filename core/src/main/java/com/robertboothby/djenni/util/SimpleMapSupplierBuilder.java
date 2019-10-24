package com.robertboothby.djenni.util;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;

import java.util.Map;
import java.util.function.Supplier;

import static com.robertboothby.djenni.core.SupplierHelper.fix;
import static java.util.stream.Collectors.toMap;

/**
 * This SupplierBuilder creates a supplier of {@link java.util.Map}s. This can be used standalone or with the
 * {@link }MapSupplierHelper} and {@link MapTypes} to create a specific Map implementation.
 * @param <K> The type of the keys,
 * @param <V> The type of the values.
 */
public class SimpleMapSupplierBuilder<K,V> implements ConfigurableSupplierBuilder<Map<K,V>, SimpleMapSupplierBuilder<K, V>> {

    private StreamableSupplier<? extends Map.Entry<? extends K, ? extends V>> entrySupplier;
    private Supplier<Integer> numberOfEntries = fix(0);

    @Override
    public StreamableSupplier<Map<K, V>> build() {
        return () -> entrySupplier.stream(numberOfEntries.get()).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Provide a Supplier of entries for the simple map. If the entry supplier may create entries with duplicate keys then
     * the number of entries in the maps produced may be reduced.
     * @param entrySupplier The Supplier.
     * @param <E> The type of the entry that will be supplied.
     * @return the SupplierBuilder for further configuration.
     */
    public <E extends Map.Entry<K,V>> SimpleMapSupplierBuilder<K, V> withEntries(StreamableSupplier<E> entrySupplier) {
        this.entrySupplier = entrySupplier;
        return this;
    }

    /**
     * Provide an Integer Supplier for the number of entries to be created in the map. If the entry supplier provides
     * duplicate keys then the numbe of entries may be reduced.
     * @param numberOfEntries the Supplier for the number of entries.
     * @return The builder for further configuration.
     */
    public SimpleMapSupplierBuilder<K, V> withNumberOfEntries(Supplier<Integer> numberOfEntries) {
        this.numberOfEntries = numberOfEntries;
        return this;
    }

    /**
     * Convenience method creating the SupplierBuilder with no configuration.
     * @param <K> The type of the keys in the supplied maps.
     * @param <V> The type of the values in the supplied maps.
     * @return The SupplierBuilder for configuration.
     */
    public static <K, V> SimpleMapSupplierBuilder<K,V> mapSupplierBuilder(){
        return new SimpleMapSupplierBuilder<>();
    }
}
