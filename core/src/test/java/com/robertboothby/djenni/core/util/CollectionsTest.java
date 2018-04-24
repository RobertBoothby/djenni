package com.robertboothby.djenni.core.util;

import org.junit.Test;

import java.util.Set;

import static com.robertboothby.djenni.core.util.Collections.asSetOfCharacters;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

public class CollectionsTest {

    @Test
    public void shouldGenerateIntegerRangeSet(){
        //Given
        int minInclusive = 3;
        int maxExclusive = 6;

        //When
        Set<Integer> range = Collections.range(minInclusive, maxExclusive);

        //Then
        assertThat(range, hasSize(3));
        assertThat(range, hasItems(3,4,5));
    }

    @Test
    public void shouldGenerateSetFromArray(){
        //Given
        String[] values = {"AB", "AA", "AC", "AA", "AD"};

        //When
        Set<String> stringSet = Collections.asSet(values);

        //Then
        assertThat(stringSet, hasSize(4));
        assertThat(stringSet, hasItems("AA", "AB", "AC", "AD"));
    }

    @Test
    public void shouldGenerateSetOfCharactersFromString(){
        //Given
        String test = "ETHEL";

        //When
        Set<Character> characters = asSetOfCharacters(test);

        //Then
        assertThat(characters, hasSize(4));
        assertThat(characters, hasItems('E', 'T', 'H', 'L'));

    }

}