package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.distribution.simple.SimpleRandomIntegerDistribution;
import com.robertboothby.djenni.matcher.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.function.Supplier;

import static com.robertboothby.djenni.core.util.Collections.asSetOfCharacters;
import static com.robertboothby.djenni.lang.CharacterSupplierBuilder.characterSupplier;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 *
 * @author robertboothby
 */
@RunWith(MockitoJUnitRunner.class)
public class CharacterSupplierBuilderTest {

    @Mock
    Distribution<Integer, Integer> distribution;

    @Test
    public void shouldUseCharactersAndGenerator() {
        //Given
        Supplier<Character> supplier = characterSupplier()
                .withCharacters("ABCDEF")
                .withDistribution(distribution)
                .build();
        given(distribution.generate(6)).willReturn(4);

        //When
        char result = supplier.get();

        //Then
        assertThat(result, is('E'));

    }

    @Test
    public void shouldEventuallyGenerateAllChars() {
        //Given
        Supplier<Character> supplier = characterSupplier()
                .withCharacters("ABCDEF")
                .withDistribution(SimpleRandomIntegerDistribution.UNIFORM)
                .build();
        //When
        //Then
        assertThat(supplier,
                Matchers.eventuallySuppliesAllValues(asSetOfCharacters("ABCDEF"), 100));
    }

    @Test
    public void builtCharacterSuppliersShouldRemainStableAfterBuilderChanges() {
        CharacterSupplierBuilder builder = characterSupplier()
                .withCharacters("A");

        Supplier<Character> supplier = builder.build();

        builder.withCharacters("BC");

        assertThat(supplier.get(), is('A'));
    }
}
