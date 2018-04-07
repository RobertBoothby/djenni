package com.robertboothby.djenni.core;

import com.robertboothby.djenni.Generator;
import org.junit.Test;

import static com.robertboothby.djenni.core.ConcatenatingStringGeneratorBuilder.generatorOfConcatenatedValues;
import static com.robertboothby.djenni.core.GeneratorHelper.buildA;
import static com.robertboothby.djenni.core.GeneratorHelper.fixedValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * <p>&#169; 2014 Forest View Developments Ltd.</p>
 *
 * @author robertboothby
 */
public class ConcatenatingStringGeneratorTest {

    @Test
    public void shouldGenerateASimpleConcatenatedString() {
        //Given
        final Generator<String> concatenatedStringGenerator = buildA(generatorOfConcatenatedValues()
                        .with(fixedValue("A"))
                        .and(fixedValue("B"))
                        .and(fixedValue("C"))
        );

        //When
        String generatedValue = concatenatedStringGenerator.generate();

        //Then
        assertThat(generatedValue, is("ABC"));
    }
}
