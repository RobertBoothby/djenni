package com.robertboothby.djenni;

import org.djenni.core.FixedValueGenerator;
import org.junit.Test;

import static org.djenni.core.GeneratorHelper.fixedValue;
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
        final Generator<String> generator = fixedValue(value);
        assertThat(generator, is(instanceOf(FixedValueGenerator.class)));
        assertThat(generator.generate(), is(equalTo(value)));
        assertThat(generator.generate(), is(theInstance(value)));
    }

}
