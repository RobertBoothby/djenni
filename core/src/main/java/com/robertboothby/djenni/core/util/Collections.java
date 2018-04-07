package com.robertboothby.djenni.core.util;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 * @author robertboothby
 */
public class Collections {
    public static Set<Integer> range(int leastInclusive, int maxExclusive) {
        Set<Integer> rangeSet = new HashSet<Integer>();
        for(int i = leastInclusive; i < maxExclusive; i++){
            rangeSet.add(i);
        }
        return rangeSet;
    }

    public static <T> Set<T> asSet(T[] valueArray) {
        Set<T> valueSet = new HashSet<T>();
        for(T value : valueArray) {
            valueSet.add(value);
        }
        return valueSet;
    }

    public static Set<Character> asSet(char[] valueArray) {
        Set<Character> valueSet = new HashSet<Character>();
        for(char value : valueArray) {
            valueSet.add(value);
        }
        return valueSet;
    }
}
