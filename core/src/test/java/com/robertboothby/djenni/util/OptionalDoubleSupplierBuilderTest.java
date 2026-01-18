package com.robertboothby.djenni.util;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.Test;

import java.util.OptionalDouble;
import java.util.concurrent.atomic.AtomicInteger;

import static com.robertboothby.djenni.core.SupplierHelper.fix;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class OptionalDoubleSupplierBuilderTest {

    @Test
    public void shouldWrapDoubleSupplierWhenPresent() {
        StreamableSupplier<OptionalDouble> optionalDoubles = OptionalDoubleSupplierBuilder.optionalDoubleSupplier()
                .valueSupplier(() -> 3.14D)
                .presentWithProbability(1.0D)
                .build();

        assertThat(optionalDoubles.get(), is(OptionalDouble.of(3.14D)));
    }

    @Test
    public void shouldReturnEmptyWithoutInvokingSupplierWhenProbabilityZero() {
        AtomicInteger invocations = new AtomicInteger();
        StreamableSupplier<OptionalDouble> optionalDoubles = OptionalDoubleSupplierBuilder.optionalDoubleSupplier()
                .valueSupplier(() -> {
                    invocations.incrementAndGet();
                    return 2.71D;
                })
                .presentWithProbability(0.0D)
                .build();

        assertThat(optionalDoubles.get(), is(OptionalDouble.empty()));
        assertThat(invocations.get(), is(0));
    }

    @Test
    public void shouldWrapStreamableSupplier() {
        StreamableSupplier<OptionalDouble> optionalDoubles = OptionalDoubleSupplierBuilder.optionalDoubleSupplier()
                .valueSupplier(fix(1.23D))
                .presentWithProbability(1.0D)
                .build();

        assertThat(optionalDoubles.get(), is(OptionalDouble.of(1.23D)));
    }

    @Test
    public void builtOptionalDoubleSuppliersShouldRemainStableAfterBuilderChanges() {
        OptionalDoubleSupplierBuilder builder = OptionalDoubleSupplierBuilder.optionalDoubleSupplier()
                .valueSupplier(() -> 1.0D)
                .presentWithProbability(1.0D);

        StreamableSupplier<OptionalDouble> supplier = builder.build();

        builder.valueSupplier(() -> 2.0D);

        assertThat(supplier.get(), is(OptionalDouble.of(1.0D)));
    }
}
