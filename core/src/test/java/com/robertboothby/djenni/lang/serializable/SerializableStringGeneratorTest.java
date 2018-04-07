package com.robertboothby.djenni.lang.serializable;

import com.robertboothby.djenni.SerializableGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

/**
 *
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 * @author robertboothby
 */
@RunWith(MockitoJUnitRunner.class)
public class SerializableStringGeneratorTest {

    @Mock private SerializableGenerator<Integer> lengthGenerator;
    @Mock private SerializableGenerator<Character> characterGenerator;

    @Test
    public void shouldUseConfiguration() {
        //Given
        SerializableStringGenerator generator = new SerializableStringGenerator(lengthGenerator, characterGenerator);
        given(lengthGenerator.generate()).willReturn(10);
        given(characterGenerator.generate())
                .willReturn('9')
                .willReturn('8')
                .willReturn('7')
                .willReturn('6')
                .willReturn('5')
                .willReturn('4')
                .willReturn('3')
                .willReturn('2')
                .willReturn('1')
                .willReturn('0');

        //When
        final String result = generator.generate();

        //Then
        assertThat(result, is("9876543210"));
    }
}
