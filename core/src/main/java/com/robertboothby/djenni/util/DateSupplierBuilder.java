package com.robertboothby.djenni.util;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.time.InstantSupplierBuilder;
import com.robertboothby.djenni.sugar.And;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Builds suppliers of {@link Date}. By default it wraps a standard {@link InstantSupplierBuilder}.
 */
public class DateSupplierBuilder implements ConfigurableSupplierBuilder<Date, DateSupplierBuilder> {

    private Supplier<Instant> instantSupplier = InstantSupplierBuilder.anInstant().build();

    public static DateSupplierBuilder dateSupplier() {
        return new DateSupplierBuilder();
    }

    @Override
    public StreamableSupplier<Date> build() {
        Supplier<Instant> actualSupplier = this.instantSupplier;
        return () -> Date.from(actualSupplier.get());
    }

    public DateSupplierBuilder instantSupplier(Supplier<Instant> supplier) {
        this.instantSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public DateSupplierBuilder instantSupplier(SupplierBuilder<Instant> builder) {
        Objects.requireNonNull(builder, "builder");
        return instantSupplier(builder.build());
    }

    public And<DateSupplierBuilder, Instant> between(Instant startInclusive) {
        return endExclusive -> {
            this.instantSupplier = InstantSupplierBuilder.anInstant().between(startInclusive).and(endExclusive).build();
            return this;
        };
    }

    public And<DateSupplierBuilder, ZonedDateTime> between(ZonedDateTime startInclusive) {
        return endExclusive -> {
            this.instantSupplier = InstantSupplierBuilder.anInstant().between(startInclusive).and(endExclusive).build();
            return this;
        };
    }
}
