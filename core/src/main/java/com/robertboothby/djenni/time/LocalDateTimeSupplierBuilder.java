package com.robertboothby.djenni.time;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
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

import static com.robertboothby.djenni.time.InstantSupplierBuilder.anInstant;

/**
 * Builds suppliers of {@link LocalDateTime} values.
 */
public class LocalDateTimeSupplierBuilder implements ConfigurableSupplierBuilder<LocalDateTime, LocalDateTimeSupplierBuilder> {

    private Supplier<Instant> instantSupplier = anInstant().build();
    private ZoneId zone = ZoneId.systemDefault();

    public static LocalDateTimeSupplierBuilder aLocalDateTime() {
        return new LocalDateTimeSupplierBuilder();
    }

    @Override
    public StreamableSupplier<LocalDateTime> build() {
        Supplier<Instant> actualSupplier = this.instantSupplier;
        ZoneId zone = this.zone;
        return () -> LocalDateTime.ofInstant(actualSupplier.get(), zone);
    }

    public LocalDateTimeSupplierBuilder instantSupplier(Supplier<Instant> supplier) {
        this.instantSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public LocalDateTimeSupplierBuilder instantSupplier(InstantSupplierBuilder builder) {
        Objects.requireNonNull(builder, "builder");
        return instantSupplier(builder.build());
    }

    public LocalDateTimeSupplierBuilder withZone(ZoneId zone) {
        this.zone = Objects.requireNonNull(zone, "zone");
        return this;
    }

    public And<LocalDateTimeSupplierBuilder, Instant> between(Instant start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<LocalDateTimeSupplierBuilder, ZonedDateTime> between(ZonedDateTime start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<LocalDateTimeSupplierBuilder, OffsetDateTime> between(OffsetDateTime start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<LocalDateTimeSupplierBuilder, LocalDateTime> between(LocalDateTime start) {
        Instant startInstant = TimeConversions.toInstant(start, zone);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end, zone));
    }

    public And<LocalDateTimeSupplierBuilder, LocalDate> between(LocalDate start) {
        Instant startInstant = TimeConversions.toInstant(start, zone);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end, zone));
    }

    public And<LocalDateTimeSupplierBuilder, Date> between(Date start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<LocalDateTimeSupplierBuilder, java.sql.Date> between(java.sql.Date start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<LocalDateTimeSupplierBuilder, Time> between(Time start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<LocalDateTimeSupplierBuilder, Timestamp> between(Timestamp start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<LocalDateTimeSupplierBuilder, Calendar> between(Calendar start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<LocalDateTimeSupplierBuilder, Long> between(Long startEpochMilli) {
        Instant startInstant = TimeConversions.toInstant(startEpochMilli);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<LocalDateTimeSupplierBuilder, Long> between(long startEpochMilli) {
        Instant startInstant = TimeConversions.toInstant(startEpochMilli);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    private LocalDateTimeSupplierBuilder configureRange(Instant startInclusive, Instant endExclusive) {
        if (!endExclusive.isAfter(startInclusive)) {
            throw new IllegalArgumentException("End instant must be after start instant.");
        }
        this.instantSupplier = anInstant().between(startInclusive).and(endExclusive).build();
        return this;
    }
}
