package com.robertboothby.djenni.core;

import org.junit.Test;


import java.util.ArrayList;
import java.util.List;

import static com.robertboothby.djenni.core.ExplicitlyBiassedSupplier.biasDetail;
import static com.robertboothby.djenni.core.ExplicitlyBiassedSupplierBuilder.explicitlyBiassedSupplierFor;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
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
        final ExplicitlyBiassedSupplierBuilder<Character> generatorBuilder
                = explicitlyBiassedSupplierFor(Character.class)
                    .addValue('A')
                    .addValue('B')
                    .addValue('C', 0.5D);

        //When
        Object builtGenerator = generatorBuilder.build();

        //Then
        assertThat(builtGenerator,is(instanceOf(ExplicitlyBiassedSupplier.class)));
        ExplicitlyBiassedSupplier<Character> actualGenerator = (ExplicitlyBiassedSupplier<Character>)builtGenerator;

        List<ExplicitlyBiassedSupplier.BiasDetail<Character>> biasList = new ArrayList<>();
        biasList.add(biasDetail('A', 1.0D));
        biasList.add(biasDetail('B', 1.0D));
        biasList.add(biasDetail('C', 0.5D));

        ExplicitlyBiassedSupplier<Character> expectedGenerator = new ExplicitlyBiassedSupplier<>(biasList);

        assertThat(actualGenerator, is(equalTo(expectedGenerator)));
    }

    @Test
    public void shouldAddValuesCorrectly(){
        //Given
        ExplicitlyBiassedSupplierBuilder<String> generatorBuilder =
                explicitlyBiassedSupplierFor(String.class)
                        .addValues("A", "B", "C")
                        .addValues(0.01D, "D", "E", "F");
        //When
        StreamableSupplier<String> actualGenerator = generatorBuilder.build();

        //Then
        List<ExplicitlyBiassedSupplier.BiasDetail<String>> biasList = new ArrayList<>();
        biasList.add(biasDetail("A", 1.0D));
        biasList.add(biasDetail("B", 1.0D));
        biasList.add(biasDetail("C", 1.0D));
        biasList.add(biasDetail("D", 0.01D));
        biasList.add(biasDetail("E", 0.01D));
        biasList.add(biasDetail("F", 0.01D));

        ExplicitlyBiassedSupplier<String> expectedGenerator = new ExplicitlyBiassedSupplier<>(biasList);

        assertThat(actualGenerator, is(equalTo(expectedGenerator)));
    }
}
