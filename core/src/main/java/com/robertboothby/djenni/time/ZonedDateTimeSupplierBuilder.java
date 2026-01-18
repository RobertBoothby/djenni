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
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.function.Supplier;

import static com.robertboothby.djenni.time.InstantSupplierBuilder.anInstant;

/**
 * Builds suppliers of {@link ZonedDateTime} values. Instants are generated via {@link InstantSupplierBuilder} and zones
 * via any {@link Supplier} (defaulting to {@link SupplierHelper#zoneIds()}).
 */
public class ZonedDateTimeSupplierBuilder implements ConfigurableSupplierBuilder<ZonedDateTime, ZonedDateTimeSupplierBuilder> {

    private Supplier<Instant> instantSupplier = anInstant().build();
    private Supplier<ZoneId> zoneSupplier = SupplierHelper.zoneIds();
    private ZoneId conversionZone = ZoneId.systemDefault();

    public static ZonedDateTimeSupplierBuilder aZonedDateTime() {
        return new ZonedDateTimeSupplierBuilder();
    }

    @Override
    public StreamableSupplier<ZonedDateTime> build() {
        Supplier<Instant> actualInstantSupplier = this.instantSupplier;
        Supplier<ZoneId> actualZoneSupplier = this.zoneSupplier;
        return () -> ZonedDateTime.ofInstant(actualInstantSupplier.get(), actualZoneSupplier.get());
    }

    public ZonedDateTimeSupplierBuilder instantSupplier(Supplier<Instant> supplier) {
        this.instantSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public ZonedDateTimeSupplierBuilder instantSupplier(InstantSupplierBuilder builder) {
        Objects.requireNonNull(builder, "builder");
        return instantSupplier(builder.build());
    }

    public ZonedDateTimeSupplierBuilder zoneSupplier(Supplier<ZoneId> supplier) {
        this.zoneSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public ZonedDateTimeSupplierBuilder fixedZone(ZoneId zone) {
        Objects.requireNonNull(zone, "zone");
        this.zoneSupplier = SupplierHelper.fix(zone);
        this.conversionZone = zone;
        return this;
    }

    public ZonedDateTimeSupplierBuilder conversionZone(ZoneId zone) {
        this.conversionZone = Objects.requireNonNull(zone, "zone");
        return this;
    }

    public And<ZonedDateTimeSupplierBuilder, Instant> between(Instant start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<ZonedDateTimeSupplierBuilder, ZonedDateTime> between(ZonedDateTime start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<ZonedDateTimeSupplierBuilder, OffsetDateTime> between(OffsetDateTime start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<ZonedDateTimeSupplierBuilder, LocalDateTime> between(LocalDateTime start) {
        Instant startInstant = TimeConversions.toInstant(start, conversionZone);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end, conversionZone));
    }

    public And<ZonedDateTimeSupplierBuilder, LocalDate> between(LocalDate start) {
        Instant startInstant = TimeConversions.toInstant(start, conversionZone);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end, conversionZone));
    }

    public And<ZonedDateTimeSupplierBuilder, Date> between(Date start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<ZonedDateTimeSupplierBuilder, java.sql.Date> between(java.sql.Date start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<ZonedDateTimeSupplierBuilder, Time> between(Time start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<ZonedDateTimeSupplierBuilder, Timestamp> between(Timestamp start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<ZonedDateTimeSupplierBuilder, Calendar> between(Calendar start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<ZonedDateTimeSupplierBuilder, Long> between(Long startEpochMilli) {
        Instant startInstant = TimeConversions.toInstant(startEpochMilli);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<ZonedDateTimeSupplierBuilder, Long> between(long startEpochMilli) {
        Instant startInstant = TimeConversions.toInstant(startEpochMilli);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    private ZonedDateTimeSupplierBuilder configureRange(Instant startInclusive, Instant endExclusive) {
        if (!endExclusive.isAfter(startInclusive)) {
            throw new IllegalArgumentException("End instant must be after start instant.");
        }
        this.instantSupplier = anInstant().between(startInclusive).and(endExclusive).build();
        return this;
    }
}
