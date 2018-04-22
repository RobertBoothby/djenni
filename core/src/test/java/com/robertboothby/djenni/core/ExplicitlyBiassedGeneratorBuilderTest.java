package com.robertboothby.djenni.core;

import org.junit.Test;


import java.util.ArrayList;
import java.util.List;

import static com.robertboothby.djenni.core.ExplicitlyBiasedGenerator.biasDetail;
import static com.robertboothby.djenni.core.ExplicitlyBiassedGeneratorBuilder.explicitlyBiassedGeneratorFor;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 *
 * @author robertboothby
 */
public class ExplicitlyBiassedGeneratorBuilderTest {

    @Test
    public void shouldGenerateExplicitlyBiassedGenerator() {
        //Given
        final ExplicitlyBiassedGeneratorBuilder<Character> generatorBuilder
                = explicitlyBiassedGeneratorFor(Character.class)
                    .addValue('A')
                    .addValue('B')
                    .addValue('C', 0.5D);

        //When
        Object builtGenerator = generatorBuilder.build();

        //Then
        assertThat(builtGenerator,is(instanceOf(ExplicitlyBiasedGenerator.class)));
        ExplicitlyBiasedGenerator<Character> actualGenerator = (ExplicitlyBiasedGenerator<Character>)builtGenerator;

        List<ExplicitlyBiasedGenerator.BiasDetail<Character>> biasList = new ArrayList<>();
        biasList.add(biasDetail('A', 1.0D));
        biasList.add(biasDetail('B', 1.0D));
        biasList.add(biasDetail('C', 0.5D));

        ExplicitlyBiasedGenerator<Character> expectedGenerator = new ExplicitlyBiasedGenerator<>(biasList);

        assertThat(actualGenerator, is(equalTo(expectedGenerator)));
    }
}
