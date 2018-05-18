package com.robertboothby.djenni.util.concurrent;

import com.robertboothby.djenni.util.MapType;

import java.util.Comparator;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConcurrentMapTypes {

    /**
     * Creates a ConcurrentHashMap MapType.
     * @param <K> The type of the map keys.
     * @param <V> The type of the map values.
     * @return A MapType instance that will create maps of the desired type.
     */
    public static <K, V> MapType<ConcurrentHashMap<K,V>, K, V> concurrentHashMap() {
        return m -> new ConcurrentHashMap<>(m);
    }

    /**
     * Creates a ConcurrentSkipListMap MapType.
     * @param <K> The type of the map keys.
     * @param <V> The type of the map values.
     * @return A MapType instance that will create maps of the desired type.
     */
    @SuppressWarnings("unchecked")
    public static <K extends Comparable, V> MapType<ConcurrentSkipListMap<K,V>, K, V> concurrentSkipListMap() {
        return m -> {
            if(m instanceof SortedMap){
                return new ConcurrentSkipListMap<>((SortedMap<K,V>)m);
            } else {
                return new ConcurrentSkipListMap<>(m);
            }
        };
    }

    /**
     * Creates a ConcurrentSkipListMap MapType using the passed in key comparator.
     * @param keyComparator The comparator to use that will impose total ordering on the keys
     * @param <K> The type of the map keys.
     * @param <V> The type of the map values.
     * @return A MapType instance that will create maps of the desired type.
     */
    @SuppressWarnings("unchecked")
    public static <K, V> MapType<ConcurrentSkipListMap<K,V>, K, V> concurrentSkipListMap(Comparator<K> keyComparator) {
        return m -> {
            ConcurrentSkipListMap<K, V> skipListMap = new ConcurrentSkipListMap<>(keyComparator);
            skipListMap.putAll(m);
            return skipListMap;
        };
    }

}
