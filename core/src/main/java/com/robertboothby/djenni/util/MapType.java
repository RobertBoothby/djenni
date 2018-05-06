package com.robertboothby.djenni.util;

import java.util.Map;

/**
 * This interface defines the contract for a set of helpers that will instantiate Maps.
 */
public interface MapType<T extends Map<K,V >, K, V> {

    /**
     * Create an instance of the represented map from a list of entries.
     * @param map A map containing the entries required.
     * @return The map instance.
     */
    T instance(Map<? extends K, ? extends V> map);
}
