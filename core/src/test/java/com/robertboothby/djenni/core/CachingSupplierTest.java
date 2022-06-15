package com.robertboothby.djenni.core;

import org.junit.Test;

import java.util.function.Supplier;

import static com.robertboothby.djenni.core.CachingSupplier.cacheSuppliedValues;
import static com.robertboothby.djenni.lang.IntegerSupplierBuilder.integerSupplier;
import static com.robertboothby.djenni.lang.StringSupplierBuilder.arbitraryString;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class CachingSupplierTest {
    @Test
    public void shouldBeAbleToConfigureLastValueSupplier(){
        //Given
        CachingSupplier<String> cachingSupplier = cacheSuppliedValues(arbitraryString().build());

        String expected = cachingSupplier.get();

        //When
        String actual = cachingSupplier.get();

        //Then
        assertThat(actual, is(sameInstance(expected)));

        //When
        cachingSupplier.next();
        String newActual = cachingSupplier.get();

        //
        assertThat(newActual, is(not(sameInstance(expected))));

    }

}
