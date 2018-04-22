package com.robertboothby.djenni.com;

import com.robertboothby.djenni.Generator;
import com.robertboothby.djenni.common.Name;
import com.robertboothby.djenni.common.NameGeneratorBuilder;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class TestNameGeneratorBuilder {

    @Test
    public void shouldCreateNameGenerator(){
        //Given

        //When
        Generator<Name> result = NameGeneratorBuilder.nameGenerator().build();

        //Then
        assertThat(result, notNullValue());

    }
}
