package com.robertboothby.djenni.core.util;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.addAll;

/**
 * Utility class holding useful method for dealing with collections particularly in context of generation.
 */
public class Collections {

    /**
     * Create a set of numbers based on the range defined.
     * @param leastInclusive the least inclusive number to use.
     * @param maxExclusive the maximum exclusive number to use.
     * @return the set with the values.
     */
    public static Set<Integer> range(int leastInclusive, int maxExclusive) {
        Set<Integer> rangeSet = new HashSet<Integer>();
        for(int i = leastInclusive; i < maxExclusive; i++){
            rangeSet.add(i);
        }
        return rangeSet;
    }

    /**
     * Create a set of values based on the array passed in ensuring uniqueness. The uniqueness is (obviously) determined
     * by the equals and hashcode methods of the objects in the array.
     * @param valueArray The array of values to turn into a set.
     * @param <T> The type of the values in the array and the resulting set.
     * @return A set of the values from the array.
     */
    public static <T> Set<T> asSet(T[] valueArray) {
        Set<T> valueSet = new HashSet<T>();
        addAll(valueSet, valueArray);
        return valueSet;
    }

    /**
     * Create a set of Characters based on the array of chars passed in, thereby ensuring uniqueness.
     * @param valueArray the array of characters to add.
     * @return a set of the Characters from the array.
     */
    public static Set<Character> asSet(char[] valueArray) {
        Set<Character> valueSet = new HashSet<>();
        for(char value : valueArray) {
            valueSet.add(value);
        }
        return valueSet;
    }
}
