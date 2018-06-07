package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.matcher.Matchers;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;

public class ByteSupplierBuilderTest {

    @Test
    public void shouldGenerateAllBytes(){
        //Given
        StreamableSupplier<Byte> supplier = ByteSupplierBuilder.byteSupplier().build();
        Set<Byte> byteSet = IntStream.range(0, 256).mapToObj(i -> (byte) i).collect(Collectors.toSet());
        //When

        //Then
        assertThat(supplier, Matchers.eventuallySuppliesAllValues(byteSet, 1000000));
    }
}
