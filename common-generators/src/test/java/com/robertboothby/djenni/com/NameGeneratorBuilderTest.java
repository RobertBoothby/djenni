package com.robertboothby.djenni.com;

import com.robertboothby.djenni.Generator;
import com.robertboothby.djenni.common.Name;
import com.robertboothby.djenni.common.NameGeneratorBuilder;
import org.junit.Test;

import static com.robertboothby.djenni.common.NameGeneratorBuilder.nameGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class NameGeneratorBuilderTest {

    @Test
    public void shouldCreateNameGenerator(){
        //Given

        //When
        Generator<Name> result = nameGenerator().build();

        //Then
        assertThat(result, notNullValue());
    }

    @Test
    public void shouldCreateGeneratorWithSuppliedNames(){
        //Given
        NameGeneratorBuilder generatorBuilder = NameGeneratorBuilder.nameGenerator(
                $ -> $.withFamilyNames("Smith")
                        .withGivenNames("John")
        );

        //When
        Name result = generatorBuilder.build().generate();

        //Then
        assertThat(result, is(new Name("John", "Smith")));
    }
}
