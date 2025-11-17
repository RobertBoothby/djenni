package com.robertboothby.djenni.util;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class OptionalSupplierBuilderTest {

    @Test
    public void shouldWrapSupplierWhenPresent() {
        StreamableSupplier<Optional<String>> optionalSupplier = OptionalSupplierBuilder.<String>optionalSupplier()
                .valueSupplier(() -> "value")
                .presentWithProbability(1.0D)
                .build();

        assertThat(optionalSupplier.get(), is(Optional.of("value")));
    }

    @Test
    public void shouldReturnEmptyWithoutTouchingSupplierWhenProbabilityZero() {
        AtomicInteger invocations = new AtomicInteger();
        StreamableSupplier<Optional<String>> optionalSupplier = OptionalSupplierBuilder.<String>optionalSupplier()
                .valueSupplier(() -> {
                    invocations.incrementAndGet();
                    return "value";
                })
                .presentWithProbability(0.0D)
                .build();

        assertThat(optionalSupplier.get(), is(Optional.empty()));
        assertThat(invocations.get(), is(0));
    }
}
