package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.SerializableGenerator;
import com.robertboothby.djenni.lang.serializable.SerializableStringGenerator;
import org.junit.Test;

import static com.robertboothby.djenni.distribution.simple.SimpleRandomIntegerDistribution.LEFT_INVERTED_NORMAL;
import static com.robertboothby.djenni.distribution.simple.SimpleRandomIntegerDistribution.RIGHT_NORMAL;
import static com.robertboothby.djenni.core.GeneratorHelper.buildA;
import static com.robertboothby.djenni.core.GeneratorHelper.buildAn;
import static com.robertboothby.djenni.lang.CharacterGeneratorBuilder.characterGenerator;
import static com.robertboothby.djenni.lang.IntegerGeneratorBuilder.integerGenerator;
import static com.robertboothby.djenni.lang.StringGeneratorBuilder.DEFAULT_AVAILABLE_CHARACTERS;
import static com.robertboothby.djenni.lang.StringGeneratorBuilder.DEFAULT_CHARACTER_SELECTION_DISTRIBUTION;
import static com.robertboothby.djenni.lang.StringGeneratorBuilder.DEFAULT_LENGTH_DISTRIBUTION;
import static com.robertboothby.djenni.lang.StringGeneratorBuilder.arbitraryString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 *
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 * @author robertboothby
 */
public class StringGeneratorBuilderTest {

    @Test
    public void shouldUseDefaults() {
        //Given
        final StringGeneratorBuilder generatorBuilder = arbitraryString();

        //When
        final SerializableGenerator<String> generator = generatorBuilder.build();

        //Then
        assertThat(generator, is(instanceOf(SerializableStringGenerator.class)));
        assertThat(
                (SerializableStringGenerator)generator,
                is(
                        new SerializableStringGenerator(
                                buildAn(integerGenerator()
                                        .between(6).and(12)
                                        .withDistribution(DEFAULT_LENGTH_DISTRIBUTION)),
                                buildA(characterGenerator()
                                        .withCharacters(DEFAULT_AVAILABLE_CHARACTERS)
                                        .withDistribution(DEFAULT_CHARACTER_SELECTION_DISTRIBUTION))
                        )
                )
        );
    }

    @Test
    public void shouldConfigureLengthRange() {
        //Given
        final StringGeneratorBuilder generatorBuilder = arbitraryString().withLengthsBetween(5).and(10);

        //When
        final SerializableGenerator<String> generator = generatorBuilder.build();

        //Then
        //Then
        assertThat(generator, is(instanceOf(SerializableStringGenerator.class)));
        assertThat(
                (SerializableStringGenerator)generator,
                is(
                        new SerializableStringGenerator(
                                buildAn(integerGenerator()
                                        .between(5).and(10)
                                        .withDistribution(DEFAULT_LENGTH_DISTRIBUTION)),
                                buildA(characterGenerator()
                                        .withCharacters(DEFAULT_AVAILABLE_CHARACTERS)
                                        .withDistribution(DEFAULT_CHARACTER_SELECTION_DISTRIBUTION))
                        )
                )
        );
    }

    @Test
    public void shouldConfigureFixedLength() {
        //Given
        final StringGeneratorBuilder generatorBuilder = arbitraryString().withFixedLength(5);

        //When
        final SerializableGenerator<String> generator = generatorBuilder.build();

        //Then
        assertThat(generator, is(instanceOf(SerializableStringGenerator.class)));
        assertThat(
                (SerializableStringGenerator)generator,
                is(
                        new SerializableStringGenerator(
                                buildAn(integerGenerator()
                                        .onlyValue(5)),
                                buildA(characterGenerator()
                                        .withCharacters(DEFAULT_AVAILABLE_CHARACTERS)
                                        .withDistribution(DEFAULT_CHARACTER_SELECTION_DISTRIBUTION))
                        )
                )
        );
    }

    @Test
    public void shouldConfigureDistributions() {
        //Given
        final StringGeneratorBuilder generatorBuilder =
                arbitraryString().withLengthDistribution(RIGHT_NORMAL).withCharacterSelectionDistribution(LEFT_INVERTED_NORMAL);

        //When
        final SerializableGenerator<String> generator = generatorBuilder.build();

        //Then
        assertThat(generator, is(instanceOf(SerializableStringGenerator.class)));
        assertThat(
                (SerializableStringGenerator)generator,
                is(
                        new SerializableStringGenerator(
                                buildAn(integerGenerator()
                                        .between(6).and(12)
                                        .withDistribution(RIGHT_NORMAL)),
                                buildA(characterGenerator()
                                        .withCharacters(DEFAULT_AVAILABLE_CHARACTERS)
                                        .withDistribution(LEFT_INVERTED_NORMAL))
                        )
                )
        );
    }

    @Test
    public void shouldConfigureAvailableCharacters() {
        //Given
        final String testCharacters = "!@£$%^&*()";
        final StringGeneratorBuilder generatorBuilder = arbitraryString().withAvailableCharacters(testCharacters);

        //When
        final SerializableGenerator<String> generator = generatorBuilder.build();

        //Then
        assertThat(generator, is(instanceOf(SerializableStringGenerator.class)));
        assertThat(
                (SerializableStringGenerator)generator,
                is(
                        new SerializableStringGenerator(
                                buildAn(integerGenerator()
                                        .between(6).and(12)
                                        .withDistribution(DEFAULT_LENGTH_DISTRIBUTION)),
                                buildA(characterGenerator()
                                        .withCharacters(testCharacters)
                                        .withDistribution(DEFAULT_CHARACTER_SELECTION_DISTRIBUTION))
                        )
                )
        );

    }
}
