package com.robertboothby.djenni.util;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.jupiter.api.Test;

import java.util.OptionalLong;
import java.util.concurrent.atomic.AtomicInteger;

import static com.robertboothby.djenni.core.SupplierHelper.fix;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class OptionalLongSupplierBuilderTest {

    @Test
    public void shouldWrapLongSupplierWhenPresent() {
        StreamableSupplier<OptionalLong> optionalLongs = OptionalLongSupplierBuilder.optionalLongSupplier()
                .valueSupplier(() -> 42L)
                .presentWithProbability(1.0D)
                .build();

        assertThat(optionalLongs.get(), is(OptionalLong.of(42L)));
    }

    @Test
    public void shouldReturnEmptyWithoutInvokingSupplierWhenProbabilityZero() {
        AtomicInteger invocations = new AtomicInteger();
        StreamableSupplier<OptionalLong> optionalLongs = OptionalLongSupplierBuilder.optionalLongSupplier()
                .valueSupplier(() -> {
                    invocations.incrementAndGet();
                    return 123L;
                })
                .presentWithProbability(0.0D)
                .build();

        assertThat(optionalLongs.get(), is(OptionalLong.empty()));
        assertThat(invocations.get(), is(0));
    }

    @Test
    public void shouldWrapStreamableSupplier() {
        StreamableSupplier<OptionalLong> optionalLongs = OptionalLongSupplierBuilder.optionalLongSupplier()
                .valueSupplier(fix(5L))
                .presentWithProbability(1.0D)
                .build();

        assertThat(optionalLongs.get(), is(OptionalLong.of(5L)));
    }

    @Test
    public void builtOptionalLongSuppliersShouldRemainStableAfterBuilderChanges() {
        OptionalLongSupplierBuilder builder = OptionalLongSupplierBuilder.optionalLongSupplier()
                .valueSupplier(() -> 8L)
                .presentWithProbability(1.0D);

        StreamableSupplier<OptionalLong> supplier = builder.build();

        builder.valueSupplier(() -> 9L);

        assertThat(supplier.get(), is(OptionalLong.of(8L)));
    }
}
