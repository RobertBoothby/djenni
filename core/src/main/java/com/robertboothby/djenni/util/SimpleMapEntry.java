package com.robertboothby.djenni.util;

import java.util.Map;

public class SimpleMapEntry<K,V> implements Map.Entry<K,V> {

    private final K key;
    private final V value;

    public SimpleMapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        throw new UnsupportedOperationException();
    }

    public static <K,V> Map.Entry<K,V> mapEntry(K key, V value){
        return new SimpleMapEntry<>(key, value);
    }

}
