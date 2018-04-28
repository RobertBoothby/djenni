package com.robertboothby.djenni.com;

import com.robertboothby.djenni.common.Name;
import com.robertboothby.djenni.common.NameSupplierBuilder;
import org.junit.Test;

import java.util.function.Supplier;

import static com.robertboothby.djenni.common.NameSupplierBuilder.nameGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class NameSupplierBuilderTest {

    @Test
    public void shouldCreateNameGenerator(){
        //Given

        //When
        Supplier<Name> result = nameGenerator().build();

        //Then
        assertThat(result, notNullValue());
    }

    @Test
    public void shouldCreateGeneratorWithSuppliedNames(){
        //Given
        NameSupplierBuilder generatorBuilder = NameSupplierBuilder.nameGenerator(
                $ -> $.withFamilyNames("Smith")
                        .withGivenNames("John")
        );

        //When
        Name result = generatorBuilder.build().get();

        //Then
        assertThat(result, is(new Name("John", "Smith")));
    }
}
