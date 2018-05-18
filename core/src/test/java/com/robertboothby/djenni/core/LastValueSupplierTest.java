package com.robertboothby.djenni.core;

import org.junit.Test;

import java.util.function.Supplier;

import static com.robertboothby.djenni.core.LastValueSupplier.lastValueSupplier;
import static com.robertboothby.djenni.core.SupplierHelper.threadLocal;
import static com.robertboothby.djenni.lang.IntegerSupplierBuilder.integerSupplier;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LastValueSupplierTest {
    @Test
    public void shouldBeAbleToConfigureLastValueSupplier(){
        //Given
        LastValueSupplier<Integer> lastValueSupplier = lastValueSupplier(
                integerSupplier($ -> $
                        .between(1)
                        .and(5)
                )
        );

        //Always use this instance as it updates the LastValueSupplier
        Supplier<Integer> source = lastValueSupplier.getSource();
        int expected = source.get();

        //When
        int actual = lastValueSupplier.get();

        //Then
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldBeAbleToConfigureThreadLocalLastValueSupplier(){
        //Given

        //Never use this instance as it does not update the LastValueSupplier
        Supplier<Integer> integerSupplier = integerSupplier($ -> $.between(6).and(10));

        ThreadLocalSupplier<Integer> threadLocalSupplier =
                threadLocal(() -> lastValueSupplier(integerSupplier));

        //Always use this reference as it updates the LastValueSupplier
        StreamableSupplier<Integer> source = ((LastValueSupplier<Integer>)threadLocalSupplier.getSupplier()).getSource();

        int expected = source.get();

        //When
        int actual = threadLocalSupplier.get();

        //Then
        assertThat(actual, is(expected));
    }
}
