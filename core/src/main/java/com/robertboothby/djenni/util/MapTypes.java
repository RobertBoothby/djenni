package com.robertboothby.djenni.util;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.WeakHashMap;

public class MapTypes {

    @SuppressWarnings("unchecked")
    public static <T extends Map<K, V>, K, V> MapType<Map<K, V>, K, V> asMap(MapType<T, K, V> underlying){
        return (MapType<Map<K, V>, K, V>) underlying;
    }

    @SuppressWarnings("unchecked")
    public static <T extends NavigableMap<K, V>, K, V> MapType<NavigableMap<K, V>, K, V> asNavigableMap(MapType<T, K, V> underlying){
        return (MapType<NavigableMap<K, V>, K, V>) underlying;
    }

    @SuppressWarnings("unchecked")
    public static <T extends SortedMap<K, V>, K, V> MapType<SortedMap<K, V>, K, V> asSortedMap(MapType<T, K, V> underlying){
        return (MapType<SortedMap<K, V>, K, V>) underlying;
    }

    /**
     * Creates an EnumMap MapType. Use this method when you are certain that there will always be keys in the map at
     * creation.
     * @param <K> The type of the key which must be an enum.
     * @param <V> The type of the value in the map.
     * @return A MapType instance that will create maps of the desired type.
     */
    @SuppressWarnings("unchecked")
    public static <K extends Enum<K>, V> MapType<EnumMap<K,V>, K, V> enumMap(){
        return m -> new EnumMap<K, V>((Map<K, ? extends V>) m );
    }

    /**
     * Creates an EnumMap MapType. Use this method when you want to handle cases where there may be no entries in the
     * map at creation.
     * @param keyClass The class of the key that will be used in the map.
     * @param <K> The type of the key which must be an enum.
     * @param <V> The type of the value in the map.
     * @return A MapType instance that will create maps of the desired type.
     */
    public static <K extends Enum<K>, V> MapType<EnumMap<K,V>, K, V> enumMap(Class<K> keyClass){
        return map -> {
            EnumMap<K, V> enumMap = new EnumMap<>(keyClass);
            enumMap.putAll(map);
            return enumMap;
        };
    }

    /**
     * Creates a HashMap MapType.
     * @param <K> The type of the map keys.
     * @param <V> The type of the map values.
     * @return A MapType instance that will create maps of the desired type.
     */
    public static <K, V> MapType<HashMap<K,V>, K, V> hashMap() {
        return m -> new HashMap<>(m);
    }

    public static <K, V> MapType<IdentityHashMap<K, V>, K, V> identityHashMap() {
        return m -> new IdentityHashMap<>(m);
    }

    public static <K, V> MapType<LinkedHashMap<K, V>, K, V> linkedHashMap() {
        return m -> new LinkedHashMap<>(m);
    }

    public static <K,V> MapType<TreeMap<K,V>, K, V> treeMap() {
        return m -> {
            if (m instanceof SortedMap) {
                return new TreeMap<>((SortedMap<K,V>)m);
            } else {
                return new TreeMap<>(m);
            }
        };
    }

    public static <K,V> MapType<TreeMap<K,V>, K, V> treeMap(Comparator<K> comparator) {
        return map -> {
            TreeMap<K, V> treeMap = new TreeMap<>(comparator);
            treeMap.putAll(map);
            return treeMap;
        };
    }

    public static <K, V> MapType<WeakHashMap<K, V>, K, V> weakHashMap() {
        return m -> new WeakHashMap<>(m);
    }

}
