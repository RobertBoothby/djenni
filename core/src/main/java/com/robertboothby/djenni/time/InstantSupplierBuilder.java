package com.robertboothby.djenni.time;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.lang.LongSupplierBuilder;
import com.robertboothby.djenni.sugar.And;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Flexible builder for {@link Instant} suppliers supporting a wide range of time representations.
 */
public class InstantSupplierBuilder implements ConfigurableSupplierBuilder<Instant, InstantSupplierBuilder> {

    private Supplier<Long> epochMilliSupplier = LongSupplierBuilder.generateALong().build();
    private ZoneId zone = ZoneId.systemDefault();

    public static InstantSupplierBuilder anInstant() {
        return new InstantSupplierBuilder();
    }

    @Override
    public StreamableSupplier<Instant> build() {
        Supplier<Long> epochMilliSupplier = this.epochMilliSupplier;
        return () -> Instant.ofEpochMilli(epochMilliSupplier.get());
    }

    public InstantSupplierBuilder epochMilliSupplier(Supplier<Long> supplier) {
        this.epochMilliSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public InstantSupplierBuilder withZone(ZoneId zone) {
        this.zone = Objects.requireNonNull(zone, "zone");
        return this;
    }

    public And<InstantSupplierBuilder, Instant> between(Instant start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return endExclusive -> configureRange(startInstant, TimeConversions.toInstant(endExclusive));
    }

    public And<InstantSupplierBuilder, ZonedDateTime> between(ZonedDateTime start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return endExclusive -> configureRange(startInstant, TimeConversions.toInstant(endExclusive));
    }

    public And<InstantSupplierBuilder, OffsetDateTime> between(OffsetDateTime start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return endExclusive -> configureRange(startInstant, TimeConversions.toInstant(endExclusive));
    }

    public And<InstantSupplierBuilder, LocalDateTime> between(LocalDateTime start) {
        Instant startInstant = TimeConversions.toInstant(start, zone);
        return endExclusive -> configureRange(startInstant, TimeConversions.toInstant(endExclusive, zone));
    }

    public And<InstantSupplierBuilder, LocalDate> between(LocalDate start) {
        Instant startInstant = TimeConversions.toInstant(start, zone);
        return endExclusive -> configureRange(startInstant, TimeConversions.toInstant(endExclusive, zone));
    }

    public And<InstantSupplierBuilder, Date> between(Date start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return endExclusive -> configureRange(startInstant, TimeConversions.toInstant(endExclusive));
    }

    public And<InstantSupplierBuilder, java.sql.Date> between(java.sql.Date start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return endExclusive -> configureRange(startInstant, TimeConversions.toInstant(endExclusive));
    }

    public And<InstantSupplierBuilder, Time> between(Time start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return endExclusive -> configureRange(startInstant, TimeConversions.toInstant(endExclusive));
    }

    public And<InstantSupplierBuilder, Timestamp> between(Timestamp start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return endExclusive -> configureRange(startInstant, TimeConversions.toInstant(endExclusive));
    }

    public And<InstantSupplierBuilder, Calendar> between(Calendar start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return endExclusive -> configureRange(startInstant, TimeConversions.toInstant(endExclusive));
    }

    public And<InstantSupplierBuilder, Long> between(Long startEpochMilli) {
        Instant startInstant = TimeConversions.toInstant(startEpochMilli);
        return endExclusive -> configureRange(startInstant, TimeConversions.toInstant(endExclusive));
    }

    public And<InstantSupplierBuilder, Long> between(long startEpochMilli) {
        Instant startInstant = TimeConversions.toInstant(startEpochMilli);
        return endExclusive -> configureRange(startInstant, TimeConversions.toInstant(endExclusive));
    }

    private InstantSupplierBuilder configureRange(Instant startInclusive, Instant endExclusive) {
        if (!endExclusive.isAfter(startInclusive)) {
            throw new IllegalArgumentException("End instant must be after start instant.");
        }
        this.epochMilliSupplier = LongSupplierBuilder.generateALong()
                .between(startInclusive.toEpochMilli())
                .and(endExclusive.toEpochMilli())
                .build();
        return this;
    }
}
