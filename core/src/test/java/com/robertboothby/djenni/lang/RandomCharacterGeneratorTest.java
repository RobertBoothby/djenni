package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.matcher.Matchers;
import org.djenni.SerializableGenerator;
import org.djenni.core.util.Collections;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.djenni.core.GeneratorHelper.buildAn;
import static org.djenni.lang.IntegerGeneratorBuilder.integerGenerator;
import static org.djenni.core.util.Collections.asSet;
import static org.djenni.matcher.Matchers.eventuallyGeneratesAllValues;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 *
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
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
