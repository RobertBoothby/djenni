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
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.function.Supplier;

import static com.robertboothby.djenni.time.InstantSupplierBuilder.anInstant;

/**
 * Builds suppliers of {@link OffsetTime}.
 */
public class OffsetTimeSupplierBuilder implements ConfigurableSupplierBuilder<OffsetTime, OffsetTimeSupplierBuilder> {

    private Supplier<Instant> instantSupplier = anInstant().build();
    private Supplier<ZoneOffset> offsetSupplier = SupplierHelper.fix(ZoneOffset.UTC);
    private ZoneId conversionZone = ZoneId.systemDefault();

    public static OffsetTimeSupplierBuilder anOffsetTime() {
        return new OffsetTimeSupplierBuilder();
    }

    @Override
    public StreamableSupplier<OffsetTime> build() {
        Supplier<Instant> actualInstantSupplier = this.instantSupplier;
        Supplier<ZoneOffset> actualOffsetSupplier = this.offsetSupplier;
        return () -> OffsetTime.ofInstant(actualInstantSupplier.get(), actualOffsetSupplier.get());
    }

    public OffsetTimeSupplierBuilder instantSupplier(Supplier<Instant> supplier) {
        this.instantSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public OffsetTimeSupplierBuilder instantSupplier(InstantSupplierBuilder builder) {
        Objects.requireNonNull(builder, "builder");
        return instantSupplier(builder.build());
    }

    public OffsetTimeSupplierBuilder offsetSupplier(Supplier<ZoneOffset> supplier) {
        this.offsetSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public OffsetTimeSupplierBuilder fixedOffset(ZoneOffset offset) {
        Objects.requireNonNull(offset, "offset");
        this.offsetSupplier = SupplierHelper.fix(offset);
        this.conversionZone = offset;
        return this;
    }

    public OffsetTimeSupplierBuilder conversionZone(ZoneId zone) {
        this.conversionZone = Objects.requireNonNull(zone, "zone");
        return this;
    }

    public And<OffsetTimeSupplierBuilder, Instant> between(Instant start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<OffsetTimeSupplierBuilder, ZonedDateTime> between(ZonedDateTime start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<OffsetTimeSupplierBuilder, OffsetDateTime> between(OffsetDateTime start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<OffsetTimeSupplierBuilder, LocalDateTime> between(LocalDateTime start) {
        Instant startInstant = TimeConversions.toInstant(start, conversionZone);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end, conversionZone));
    }

    public And<OffsetTimeSupplierBuilder, LocalDate> between(LocalDate start) {
        Instant startInstant = TimeConversions.toInstant(start, conversionZone);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end, conversionZone));
    }

    public And<OffsetTimeSupplierBuilder, Date> between(Date start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<OffsetTimeSupplierBuilder, java.sql.Date> between(java.sql.Date start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<OffsetTimeSupplierBuilder, Time> between(Time start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<OffsetTimeSupplierBuilder, Timestamp> between(Timestamp start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<OffsetTimeSupplierBuilder, Calendar> between(Calendar start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<OffsetTimeSupplierBuilder, Long> between(Long startEpochMilli) {
        Instant startInstant = TimeConversions.toInstant(startEpochMilli);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<OffsetTimeSupplierBuilder, Long> between(long startEpochMilli) {
        Instant startInstant = TimeConversions.toInstant(startEpochMilli);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    private OffsetTimeSupplierBuilder configureRange(Instant startInclusive, Instant endExclusive) {
        if (!endExclusive.isAfter(startInclusive)) {
            throw new IllegalArgumentException("End instant must be after start instant.");
        }
        this.instantSupplier = anInstant().between(startInclusive).and(endExclusive).build();
        return this;
    }
}
