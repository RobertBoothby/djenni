package com.robertboothby.djenni.core.util;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        return IntStream.range(leastInclusive, maxExclusive).boxed().collect(Collectors.toSet());
    }

    /**
     * Create a set of values based on the array passed in ensuring uniqueness. The uniqueness is (obviously) determined
     * by the equals and hashcode methods of the objects in the array.
     * @param valueArray The array of values to turn into a set.
     * @param <T> The type of the values in the array and the resulting set.
     * @return A set of the values from the array.
     */
    public static <T> Set<T> asSet(T ... valueArray) {
        Set<T> valueSet = new HashSet<T>();
        addAll(valueSet, valueArray);
        return valueSet;
    }

    /**
     * Create a set of Characters based on the array of chars passed in, thereby ensuring uniqueness.
     * @param charSequence the character sequence to base the array on..
     * @return a set of the Characters from the array.
     */
    public static Set<Character> asSetOfCharacters(CharSequence charSequence) {
        return charSequence
                .chars()
                .mapToObj(i -> (char) i)
                .collect(Collectors.toSet());
    }

}
