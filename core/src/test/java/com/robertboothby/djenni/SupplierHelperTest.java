package com.robertboothby.djenni;

import com.robertboothby.djenni.core.SupplierHelper;
import org.junit.Test;

import java.util.function.Supplier;

import static com.robertboothby.djenni.core.SupplierHelper.fix;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.theInstance;

/**
 * Test the helper methods.
 * @author robertboothby
 */
public class SupplierHelperTest {

    @Test
    public void fixedGeneratorShouldReturnExactObject() {
        String value = "Test Value";
        final Supplier<String> generator = fix(value);
        assertThat(generator.get(), is(equalTo(value)));
        assertThat(generator.get(), is(theInstance(value)));
    }

    @Test
    public void shouldGenerateFromArray(){
        //Given
        String charString = "ABCDEF";

        //When
        Supplier<Character> generator = SupplierHelper.fromValues(charString.toCharArray());

        //Then

    }

}
