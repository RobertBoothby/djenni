package com.robertboothby.djenni.time;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.core.SupplierHelper;
import com.robertboothby.djenni.sugar.And;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.function.Supplier;

import static com.robertboothby.djenni.time.InstantSupplierBuilder.anInstant;

/**
 * Builds suppliers of {@link OffsetDateTime}.
 */
public class OffsetDateTimeSupplierBuilder implements ConfigurableSupplierBuilder<OffsetDateTime, OffsetDateTimeSupplierBuilder> {

    private Supplier<Instant> instantSupplier = anInstant().build();
    private Supplier<ZoneOffset> offsetSupplier = SupplierHelper.fix(ZoneOffset.UTC);
    private ZoneId conversionZone = ZoneId.systemDefault();

    public static OffsetDateTimeSupplierBuilder anOffsetDateTime() {
        return new OffsetDateTimeSupplierBuilder();
    }

    @Override
    public StreamableSupplier<OffsetDateTime> build() {
        Supplier<Instant> actualInstantSupplier = this.instantSupplier;
        Supplier<ZoneOffset> actualOffsetSupplier = this.offsetSupplier;
        return () -> OffsetDateTime.ofInstant(actualInstantSupplier.get(), actualOffsetSupplier.get());
    }

    public OffsetDateTimeSupplierBuilder instantSupplier(Supplier<Instant> supplier) {
        this.instantSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public OffsetDateTimeSupplierBuilder instantSupplier(InstantSupplierBuilder builder) {
        Objects.requireNonNull(builder, "builder");
        return instantSupplier(builder.build());
    }

    public OffsetDateTimeSupplierBuilder offsetSupplier(Supplier<ZoneOffset> supplier) {
        this.offsetSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public OffsetDateTimeSupplierBuilder fixedOffset(ZoneOffset offset) {
        Objects.requireNonNull(offset, "offset");
        this.offsetSupplier = SupplierHelper.fix(offset);
        this.conversionZone = offset;
        return this;
    }

    public OffsetDateTimeSupplierBuilder conversionZone(ZoneId zone) {
        this.conversionZone = Objects.requireNonNull(zone, "zone");
        return this;
    }

    public And<OffsetDateTimeSupplierBuilder, Instant> between(Instant start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<OffsetDateTimeSupplierBuilder, ZonedDateTime> between(ZonedDateTime start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<OffsetDateTimeSupplierBuilder, OffsetDateTime> between(OffsetDateTime start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<OffsetDateTimeSupplierBuilder, LocalDateTime> between(LocalDateTime start) {
        Instant startInstant = TimeConversions.toInstant(start, conversionZone);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end, conversionZone));
    }

    public And<OffsetDateTimeSupplierBuilder, LocalDate> between(LocalDate start) {
        Instant startInstant = TimeConversions.toInstant(start, conversionZone);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end, conversionZone));
    }

    public And<OffsetDateTimeSupplierBuilder, Date> between(Date start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<OffsetDateTimeSupplierBuilder, java.sql.Date> between(java.sql.Date start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<OffsetDateTimeSupplierBuilder, Time> between(Time start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<OffsetDateTimeSupplierBuilder, Timestamp> between(Timestamp start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<OffsetDateTimeSupplierBuilder, Calendar> between(Calendar start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<OffsetDateTimeSupplierBuilder, Long> between(Long startEpochMilli) {
        Instant startInstant = TimeConversions.toInstant(startEpochMilli);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<OffsetDateTimeSupplierBuilder, Long> between(long startEpochMilli) {
        Instant startInstant = TimeConversions.toInstant(startEpochMilli);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    private OffsetDateTimeSupplierBuilder configureRange(Instant startInclusive, Instant endExclusive) {
        if (!endExclusive.isAfter(startInclusive)) {
            throw new IllegalArgumentException("End instant must be after start instant.");
        }
        this.instantSupplier = anInstant().between(startInclusive).and(endExclusive).build();
        return this;
    }
}
