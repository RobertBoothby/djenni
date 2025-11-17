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
 * Builds suppliers of {@link LocalDate} values with rich range configuration support.
 */
public class LocalDateSupplierBuilder implements ConfigurableSupplierBuilder<LocalDate, LocalDateSupplierBuilder> {

    private Supplier<Instant> instantSupplier = anInstant().build();
    private ZoneId zone = ZoneId.systemDefault();

    public static LocalDateSupplierBuilder aLocalDate() {
        return new LocalDateSupplierBuilder();
    }

    @Override
    public StreamableSupplier<LocalDate> build() {
        Supplier<Instant> actualInstantSupplier = this.instantSupplier;
        ZoneId zone = this.zone;
        return () -> LocalDateTime.ofInstant(actualInstantSupplier.get(), zone).toLocalDate();
    }

    public LocalDateSupplierBuilder instantSupplier(Supplier<Instant> supplier) {
        this.instantSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public LocalDateSupplierBuilder instantSupplier(InstantSupplierBuilder builder) {
        Objects.requireNonNull(builder, "builder");
        return instantSupplier(builder.build());
    }

    public LocalDateSupplierBuilder withZone(ZoneId zone) {
        this.zone = Objects.requireNonNull(zone, "zone");
        return this;
    }

    public And<LocalDateSupplierBuilder, Instant> between(Instant start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<LocalDateSupplierBuilder, ZonedDateTime> between(ZonedDateTime start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<LocalDateSupplierBuilder, OffsetDateTime> between(OffsetDateTime start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<LocalDateSupplierBuilder, LocalDateTime> between(LocalDateTime start) {
        Instant startInstant = TimeConversions.toInstant(start, zone);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end, zone));
    }

    public And<LocalDateSupplierBuilder, LocalDate> between(LocalDate start) {
        Instant startInstant = TimeConversions.toInstant(start, zone);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end, zone));
    }

    public And<LocalDateSupplierBuilder, Date> between(Date start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<LocalDateSupplierBuilder, java.sql.Date> between(java.sql.Date start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<LocalDateSupplierBuilder, Time> between(Time start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<LocalDateSupplierBuilder, Timestamp> between(Timestamp start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<LocalDateSupplierBuilder, Calendar> between(Calendar start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<LocalDateSupplierBuilder, Long> between(Long startEpochMilli) {
        Instant startInstant = TimeConversions.toInstant(startEpochMilli);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<LocalDateSupplierBuilder, Long> between(long startEpochMilli) {
        Instant startInstant = TimeConversions.toInstant(startEpochMilli);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    private LocalDateSupplierBuilder configureRange(Instant startInclusive, Instant endExclusive) {
        if (!endExclusive.isAfter(startInclusive)) {
            throw new IllegalArgumentException("End instant must be after start instant.");
        }
        this.instantSupplier = anInstant().between(startInclusive).and(endExclusive).build();
        return this;
    }
}
