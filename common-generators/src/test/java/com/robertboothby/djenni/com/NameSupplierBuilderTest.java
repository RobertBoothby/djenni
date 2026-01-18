package com.robertboothby.djenni.com;

import com.robertboothby.djenni.common.Name;
import com.robertboothby.djenni.common.NameSupplierBuilder;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class NameSupplierBuilderTest {

    @Test
    public void shouldCreateNameSupplier(){
        //Given

        //When
        Supplier<Name> result = NameSupplierBuilder.nameSupplierBuilder().build();

        //Then
        assertThat(result, notNullValue());
    }

    @Test
    public void shouldCreateSupplierWithNewNames(){
        //Given
        Supplier<Name> nameSupplier = NameSupplierBuilder.names(
                $ -> $.withFamilyNames("Smith")
                        .withGivenNames("John")
        );

        //When
        Name result = nameSupplier.get();

        //Then
        assertThat(result, is(new Name("John", "Smith")));
    }
}
