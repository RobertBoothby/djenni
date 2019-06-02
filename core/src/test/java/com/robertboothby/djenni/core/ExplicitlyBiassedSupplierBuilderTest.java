package com.robertboothby.djenni.core;

import com.robertboothby.djenni.core.util.Collections;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static com.robertboothby.djenni.core.ExplicitlyBiassedSupplierBuilder.explicitlyBiassedSupplierFor;
import static com.robertboothby.djenni.core.SupplierHelper.fix;
import static com.robertboothby.djenni.core.util.Collections.asSetOfCharacters;
import static com.robertboothby.djenni.distribution.simple.SimpleRandomDoubleDistribution.UNIFORM;
import static com.robertboothby.djenni.matcher.Matchers.eventuallySuppliesAllValues;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 *
 * @author robertboothby
 */
public class ExplicitlyBiassedSupplierBuilderTest {

    @Test
    @SuppressWarnings("unchecked")
    public void shouldGenerateExplicitlyBiassedGenerator() {
        //Given
        final ExplicitlyBiassedSupplierBuilder<Character> supplierBuilder
                = explicitlyBiassedSupplierFor(Character.class)
                    .addSupplier(fix('A'))
                    .addSupplier(fix('B'))
                    .addSupplier(fix('C'), 0.5D);

        //When
        ExplicitlyBiassedSupplier<Character> actualSupplier = supplierBuilder.build();

        //Then
        assertThat(actualSupplier.proportionsTotal, is(2.5D));
        assertThat(actualSupplier.distribution, is(UNIFORM));
        assertThat(actualSupplier, eventuallySuppliesAllValues(Collections.asSet('A', 'B', 'C'), 100));
    }

    @Test
    public void shouldAddValuesCorrectly(){
        //Given
        StreamableSupplier<String> a = fix("A");
        StreamableSupplier<String> b = fix("B");
        StreamableSupplier<String> c = fix("C");
        StreamableSupplier<String> d = fix("D");
        StreamableSupplier<String> e = fix("E");
        StreamableSupplier<String> f = fix("F");
        ExplicitlyBiassedSupplierBuilder<String> supplierBuilder =
                explicitlyBiassedSupplierFor(String.class)
                        .addSuppliers(a, b, c)
                        .addSuppliers(0.4D, d, e, f);
        //When
        ExplicitlyBiassedSupplier<String> actualSupplier = supplierBuilder.build();

        //Then
        assertThat(actualSupplier.proportionsTotal, is(4.2D));
        assertThat(actualSupplier.distribution, is(UNIFORM));
        assertThat(actualSupplier, eventuallySuppliesAllValues(Collections.asSet("A", "B", "C", "D", "E", "F"), 1000));
    }
}
