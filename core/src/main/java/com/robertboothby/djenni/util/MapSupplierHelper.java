package com.robertboothby.djenni.util;

import com.robertboothby.djenni.core.StreamableSupplier;

import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This class contains static utility methods to make it easier to work with Maps.
 */
public class MapSupplierHelper {

    /**
     * Create a supplier of Map.Entry based on the key and value suppliers passed in.
     * @param keys The supplier of keys.
     * @param values The supplier of values.
     * @param <K> The type of the keys.
     * @param <V> The type of the values.
     * @return A supplier of map entries.
     */
    public static <K,V> Supplier<Map.Entry<? extends K,? extends V>> supplyEntries(Supplier<? extends K> keys, Supplier<? extends V> values){
        return () -> new AbstractMap.SimpleImmutableEntry<>(keys.get(), values.get());
    }

    /**
     * Create a supplier of Map.Entry based on a key supplier and a function that derives the value from the key.
     * @param keys The key supplier.
     * @param values The function that will derive the value from the key
     * @param <K> The type of the key.
     * @param <V> THe type of the value.
     * @return A supplier of map entries.
     */
    public static <K,V> StreamableSupplier<Map.Entry<K, V>> supplyEntries(Supplier<K> keys, Function<K, V> values) {
        return () -> {
            K key = keys.get();
            return new AbstractMap.SimpleImmutableEntry<>(key, values.apply(key));
        };
    }

    /**
     * Create a supplier of Map.Entry based on a value supplier and a function that derives the key from the value.
     * @param keys The function that will derive the key from the value.
     * @param values The value supplier.
     * @param <K> The type of the key.
     * @param <V> THe type of the value.
     * @return A supplier of map entries.
     */
    public static <K,V> Supplier<Map.Entry<K,V>> supplyEntries(Function<V,K> keys, Supplier<V> values) {
        return () -> {
            V value = values.get();
            return new AbstractMap.SimpleImmutableEntry<>(keys.apply(value), value);
        };
    }
}
