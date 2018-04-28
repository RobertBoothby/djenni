package com.robertboothby.djenni.core;

import org.junit.Test;

import java.util.function.Supplier;

import static com.robertboothby.djenni.core.ConcatenatingStringSupplierBuilder.supplierOfConcatenatedValues;
import static com.robertboothby.djenni.core.SupplierHelper.fix;
import static com.robertboothby.djenni.core.SupplierHelper.buildA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 *
 * @author robertboothby
 */
public class ConcatenatingStringSupplierBuilderTest {

    @Test
    public void shouldGenerateASimpleConcatenatedString() {
        //Given
        final Supplier<String> concatenatedStringGenerator = buildA(supplierOfConcatenatedValues()
                .with(fix("A"))
                .and(fix("B"))
                .and(fix("C"))
        );

        //When
        String generatedValue = concatenatedStringGenerator.get();

        //Then
        assertThat(generatedValue, is("ABC"));
    }
}
