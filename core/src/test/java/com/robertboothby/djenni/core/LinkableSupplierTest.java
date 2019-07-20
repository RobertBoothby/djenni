package com.robertboothby.djenni.core;

import org.junit.Test;

import java.util.function.Supplier;

import static com.robertboothby.djenni.core.LinkableSupplier.linkable;
import static com.robertboothby.djenni.core.SupplierHelper.threadLocal;
import static com.robertboothby.djenni.lang.IntegerSupplierBuilder.integerSupplier;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LinkableSupplierTest {
    @Test
    public void shouldBeAbleToConfigureLastValueSupplier(){
        //Given
        LinkableSupplier<Integer> linkableSupplier = linkable(
                integerSupplier($ -> $
                        .between(1)
                        .and(5)
                )
        );

        //Always use this instance as it updates the LinkableSupplier
        Supplier<Integer> linkedSupplier = linkableSupplier.linked();
        int expected = linkableSupplier.get();

        //When
        int actual = linkedSupplier.get();

        //Then
        assertThat(actual, is(expected));
    }

}
