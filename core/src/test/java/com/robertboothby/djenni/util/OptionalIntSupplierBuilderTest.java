package com.robertboothby.djenni.util;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.jupiter.api.Test;

import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicInteger;

import static com.robertboothby.djenni.core.SupplierHelper.fix;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class OptionalIntSupplierBuilderTest {

    @Test
    public void shouldWrapIntSupplierWhenPresent() {
        StreamableSupplier<OptionalInt> optionalInts = OptionalIntSupplierBuilder.optionalIntSupplier()
                .valueSupplier(() -> 7)
                .presentWithProbability(1.0D)
                .build();

        assertThat(optionalInts.get(), is(OptionalInt.of(7)));
    }

    @Test
    public void shouldReturnEmptyWithoutInvokingSupplierWhenProbabilityZero() {
        AtomicInteger invocations = new AtomicInteger();
        StreamableSupplier<OptionalInt> optionalInts = OptionalIntSupplierBuilder.optionalIntSupplier()
                .valueSupplier(() -> {
                    invocations.incrementAndGet();
                    return 99;
                })
                .presentWithProbability(0.0D)
                .build();

        assertThat(optionalInts.get(), is(OptionalInt.empty()));
        assertThat(invocations.get(), is(0));
    }

    @Test
    public void shouldWrapStreamableSupplier() {
        StreamableSupplier<OptionalInt> optionalInts = OptionalIntSupplierBuilder.optionalIntSupplier()
                .valueSupplier(fix(11))
                .presentWithProbability(1.0D)
                .build();

        assertThat(optionalInts.get(), is(OptionalInt.of(11)));
    }

    @Test
    public void builtOptionalIntSuppliersShouldRemainStableAfterBuilderChanges() {
        OptionalIntSupplierBuilder builder = OptionalIntSupplierBuilder.optionalIntSupplier()
                .valueSupplier(() -> 5)
                .presentWithProbability(1.0D);

        StreamableSupplier<OptionalInt> supplier = builder.build();

        builder.valueSupplier(() -> 10);

        assertThat(supplier.get(), is(OptionalInt.of(5)));
    }
}
