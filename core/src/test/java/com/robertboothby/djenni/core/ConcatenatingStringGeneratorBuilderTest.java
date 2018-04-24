package com.robertboothby.djenni.core;

import com.robertboothby.djenni.Generator;
import org.junit.Test;

import static com.robertboothby.djenni.core.ConcatenatingStringGeneratorBuilder.generatorOfConcatenatedValues;
import static com.robertboothby.djenni.core.GeneratorHelper.$;
import static com.robertboothby.djenni.core.GeneratorHelper.buildA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 *
 * @author robertboothby
 */
public class ConcatenatingStringGeneratorBuilderTest {

    @Test
    public void shouldGenerateASimpleConcatenatedString() {
        //Given
        final Generator<String> concatenatedStringGenerator = buildA(generatorOfConcatenatedValues()
                .with($("A"))
                .and($("B"))
                .and($("C"))
        );

        //When
        String generatedValue = concatenatedStringGenerator.generate();

        //Then
        assertThat(generatedValue, is("ABC"));
    }
}
