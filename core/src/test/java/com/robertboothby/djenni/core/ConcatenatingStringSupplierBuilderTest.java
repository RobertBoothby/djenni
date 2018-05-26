package com.robertboothby.djenni.core;

import com.robertboothby.djenni.util.SimpleListSupplierBuilder;
import org.junit.Test;

import java.util.List;
import java.util.function.Supplier;

import static com.robertboothby.djenni.core.ConcatenatingStringSupplierBuilder.supplierOfConcatenatedValues;
import static com.robertboothby.djenni.core.SupplierHelper.fix;
import static com.robertboothby.djenni.util.SimpleListSupplierBuilder.simpleList;
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

    @Test
    public void shouldGenerateAComplexConcatenatedString(){

        //Given
        final StreamableSupplier<String> concatenatedStringGenerator = supplierOfConcatenatedValues(
                b -> {
                    b
                            .with(fix("A"))
                            .and( fix(" "))
                            .withList(fix("[ "), fix(" ]"), fix(", "),
                                    simpleList($ -> $.size(5).entries(fix("B"))));
                }
        );
        //When
        String generatedValue = concatenatedStringGenerator.get();

        //Then
        assertThat(generatedValue, is("A [ B, B, B, B, B ]"));

    }
}
