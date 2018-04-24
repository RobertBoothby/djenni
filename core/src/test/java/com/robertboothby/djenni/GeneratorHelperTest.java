package com.robertboothby.djenni;

import com.robertboothby.djenni.core.FixedValueGenerator;
import com.robertboothby.djenni.core.GeneratorHelper;
import org.junit.Test;

import static com.robertboothby.djenni.core.GeneratorHelper.$;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.theInstance;
import static org.junit.Assert.assertThat;

/**
 * Test the helper methods.
 * @author robertboothby
 */
public class GeneratorHelperTest {

    @Test
    public void fixedGeneratorShouldReturnExactObject() {
        String value = "Test Value";
        final Generator<String> generator = $(value);
        assertThat(generator, is(instanceOf(FixedValueGenerator.class)));
        assertThat(generator.generate(), is(equalTo(value)));
        assertThat(generator.generate(), is(theInstance(value)));
    }

    @Test
    public void shouldGenerateFromArray(){
        //Given
        String charString = "ABCDEF";

        //When
        SerializableGenerator<Character> generator = GeneratorHelper.fromValues(charString.toCharArray());

        //Then

    }

}
