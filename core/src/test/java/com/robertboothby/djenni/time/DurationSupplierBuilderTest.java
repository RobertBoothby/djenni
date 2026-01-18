package com.robertboothby.djenni.time;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DurationSupplierBuilderTest {

    @Test
    public void shouldGenerateDurationWithinRange() {
        Duration start = Duration.ofSeconds(5);
        Duration end = Duration.ofSeconds(6);

        StreamableSupplier<Duration> supplier = DurationSupplierBuilder.aDuration()
                .between(start)
                .and(end)
                .build();

        assertThat(supplier.get(), is(start));
    }

    @Test
    public void shouldRejectInvalidRange() {
        assertThrows(IllegalArgumentException.class, () -> DurationSupplierBuilder.aDuration()
                .between(Duration.ofSeconds(1))
                .and(Duration.ofSeconds(1)));
    }

    @Test
    public void builtSuppliersShouldNotChangeWhenBuilderIsReconfigured() {
        DurationSupplierBuilder builder = DurationSupplierBuilder.aDuration()
                .secondsSupplier(() -> 10L);

        StreamableSupplier<Duration> supplier = builder.build();

        builder.secondsSupplier(() -> 20L);

        assertThat(supplier.get(), is(Duration.ofSeconds(10)));
    }
}
