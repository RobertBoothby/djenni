package com.robertboothby.djenni;

import com.robertboothby.djenni.core.FixedValueGenerator;
import org.junit.Test;

import static com.robertboothby.djenni.core.GeneratorHelper.$;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.theInstance;
import static org.junit.Assert.assertThat;

/**
 * Test the helper methods.
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
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

}
