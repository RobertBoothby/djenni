package com.robertboothby.djenni.core;

import org.junit.Test;

import java.util.function.Supplier;

import static com.robertboothby.djenni.core.ConcatenatingStringSupplierBuilder.supplierOfConcatenatedValues;
import static com.robertboothby.djenni.core.SupplierHelper.fix;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Test for the ConcatenatingStringSupplierBuilder.
 *
 * @author robertboothby.
 */
public class ConcatenatingStringSupplierBuilderTest {

    @Test
    public void shouldGenerateASimpleConcatenatedString() {
        //Given
        final StreamableSupplier<String> concatenatedStringGenerator = supplierOfConcatenatedValues(
                b -> b
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
