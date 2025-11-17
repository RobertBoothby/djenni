package com.robertboothby.djenni.time;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.lang.LongSupplierBuilder;
import com.robertboothby.djenni.sugar.And;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Builds suppliers of {@link Duration}.
 */
public class DurationSupplierBuilder implements ConfigurableSupplierBuilder<Duration, DurationSupplierBuilder> {

    private Supplier<Long> secondsSupplier = LongSupplierBuilder.generateALong().build();
    private Supplier<Long> nanosSupplier = () -> 0L;

    public static DurationSupplierBuilder aDuration() {
        return new DurationSupplierBuilder();
    }

    @Override
    public StreamableSupplier<Duration> build() {
        Supplier<Long> seconds = this.secondsSupplier;
        Supplier<Long> nanos = this.nanosSupplier;
        return () -> Duration.ofSeconds(seconds.get(), nanos.get());
    }

    public DurationSupplierBuilder secondsSupplier(Supplier<Long> supplier) {
        this.secondsSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public DurationSupplierBuilder nanosSupplier(Supplier<Long> supplier) {
        this.nanosSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public And<DurationSupplierBuilder, Duration> between(Duration minimumInclusive) {
        Objects.requireNonNull(minimumInclusive, "minimumInclusive");
        return maximumExclusive -> configureRange(minimumInclusive, maximumExclusive);
    }

    private DurationSupplierBuilder configureRange(Duration startInclusive, Duration endExclusive) {
        if (!endExclusive.minus(startInclusive).isPositive()) {
            throw new IllegalArgumentException("Maximum duration must be greater than minimum.");
        }
        this.secondsSupplier = LongSupplierBuilder.generateALong()
                .between(startInclusive.getSeconds())
                .and(endExclusive.getSeconds())
                .build();
        this.nanosSupplier = () -> 0L;
        return this;
    }
}
