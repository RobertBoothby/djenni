package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.matcher.Matchers;
import com.robertboothby.djenni.SerializableGenerator;
import com.robertboothby.djenni.core.util.Collections;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.robertboothby.djenni.core.GeneratorHelper.buildAn;
import static com.robertboothby.djenni.lang.IntegerGeneratorBuilder.integerGenerator;
import static com.robertboothby.djenni.core.util.Collections.asSet;
import static com.robertboothby.djenni.matcher.Matchers.eventuallyGeneratesAllValues;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 *
 * @author robertboothby
 */
@RunWith(MockitoJUnitRunner.class)
public class RandomCharacterGeneratorTest {

    @Mock SerializableGenerator<Integer> positionGenerator;

    @Test
    public void shouldUseCharactersAndGenerator() {
        //Given
        RandomCharacterGenerator generator =  new RandomCharacterGenerator("ABCDEF".toCharArray(), positionGenerator);
        given(positionGenerator.generate()).willReturn(4);

        //When
        char result = generator.generate();

        //Then
        assertThat(result, is('E'));

    }

    @Test
    public void shouldEventuallyGenerateAllChars() {
        //Given
        RandomCharacterGenerator generator = new RandomCharacterGenerator("ABCDEF".toCharArray(),
                buildAn(integerGenerator().between(0).and(6)));

        //When
        //Then
        assertThat(generator,
                Matchers.eventuallyGeneratesAllValues(Collections.asSet("ABCDEF".toCharArray()), 100));
    }
}
