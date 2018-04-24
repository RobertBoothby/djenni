package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.Generator;
import com.robertboothby.djenni.core.FixedValueGenerator;
import com.robertboothby.djenni.core.GeneratorHelper;
import org.junit.Test;

import static com.robertboothby.djenni.distribution.simple.SimpleRandomIntegerDistribution.INVERTED_NORMAL;
import static com.robertboothby.djenni.core.GeneratorHelper.buildA;
import static com.robertboothby.djenni.lang.CharacterGeneratorBuilder.characterGenerator;
import static com.robertboothby.djenni.lang.IntegerGeneratorBuilder.integerGenerator;
import static com.robertboothby.djenni.lang.StringGeneratorBuilder.DEFAULT_AVAILABLE_CHARACTERS;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 *
 * @author robertboothby
 */
public class CharacterGeneratorBuilderTest {

    @Test
    public void shouldConfigureDefaults() {
        //Given
        Generator<Character> generator = buildA(characterGenerator());

        //When
        //Then
        assertThat(generator, is(instanceOf(RandomCharacterGenerator.class)));
        assertThat((RandomCharacterGenerator)generator,
                is(
                        new RandomCharacterGenerator(
                                DEFAULT_AVAILABLE_CHARACTERS.toCharArray(),
                                GeneratorHelper.buildAn(integerGenerator().between(0).and(DEFAULT_AVAILABLE_CHARACTERS.length())))
                ));
    }

    @Test
    public void shouldConfigureCharactersFromString() {
        //Given
        Generator<Character> generator = buildA(characterGenerator().withCharacters("ABCDEFG"));

        //When
        //Then
        assertThat(generator, is(instanceOf(RandomCharacterGenerator.class)));
        assertThat((RandomCharacterGenerator)generator,
                is(
                        new RandomCharacterGenerator(
                                new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G'},
                                GeneratorHelper.buildAn(integerGenerator().between(0).and(7)))
                ));

    }

    @Test
    public void shouldConfigureCharactersFromArray() {
        //Given
        char[] chars = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G'};
        Generator<Character> generator = buildA(characterGenerator().withCharacters(chars));

        //When
        chars[1] = 'b'; //Check immutability.

        //Then
        assertThat(generator, is(instanceOf(RandomCharacterGenerator.class)));
        assertThat((RandomCharacterGenerator)generator,
                is(
                        new RandomCharacterGenerator(
                                new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G'},
                                GeneratorHelper.buildAn(integerGenerator().between(0).and(7)))
                ));

    }

    @Test
    public void shouldConfigureSingleCharacter() {
        //Given
        Generator<Character> generator = buildA(characterGenerator().withCharacters(new char[]{'A'}));

        //When
        //Then
        assertThat(generator, is(instanceOf(FixedValueGenerator.class)));
        assertThat(generator,
                is(
                        new FixedValueGenerator<>('A')
                ));

    }

    @Test
    public void shouldConfigureDistribution() {
        //Given
        Generator<Character> generator = buildA(characterGenerator().withDistribution(INVERTED_NORMAL));

        //When
        //Then
        assertThat(generator, is(instanceOf(RandomCharacterGenerator.class)));
        assertThat(generator,
                is(
                        new RandomCharacterGenerator(
                                DEFAULT_AVAILABLE_CHARACTERS.toCharArray(),
                                GeneratorHelper.buildAn(
                                        integerGenerator()
                                                .between(0).and(DEFAULT_AVAILABLE_CHARACTERS.length())
                                                .withDistribution(INVERTED_NORMAL)))
                ));

    }
}
