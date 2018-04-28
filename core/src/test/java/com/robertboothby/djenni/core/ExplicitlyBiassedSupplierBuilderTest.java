package com.robertboothby.djenni.core;

import org.junit.Test;


import java.util.ArrayList;
import java.util.List;

import static com.robertboothby.djenni.core.ExplicitlyBiasedSupplier.biasDetail;
import static com.robertboothby.djenni.core.ExplicitlyBiassedSupplierBuilder.explicitlyBiassedGeneratorFor;
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
    public void shouldGenerateExplicitlyBiassedGenerator() {
        //Given
        final ExplicitlyBiassedSupplierBuilder<Character> generatorBuilder
                = explicitlyBiassedGeneratorFor(Character.class)
                    .addValue('A')
                    .addValue('B')
                    .addValue('C', 0.5D);

        //When
        Object builtGenerator = generatorBuilder.build();

        //Then
        assertThat(builtGenerator,is(instanceOf(ExplicitlyBiasedSupplier.class)));
        ExplicitlyBiasedSupplier<Character> actualGenerator = (ExplicitlyBiasedSupplier<Character>)builtGenerator;

        List<ExplicitlyBiasedSupplier.BiasDetail<Character>> biasList = new ArrayList<>();
        biasList.add(biasDetail('A', 1.0D));
        biasList.add(biasDetail('B', 1.0D));
        biasList.add(biasDetail('C', 0.5D));

        ExplicitlyBiasedSupplier<Character> expectedGenerator = new ExplicitlyBiasedSupplier<>(biasList);

        assertThat(actualGenerator, is(equalTo(expectedGenerator)));
    }
}
