package com.robertboothby.djenni.time;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.sugar.And;
import org.threeten.extra.chrono.CopticChronology;
import org.threeten.extra.chrono.CopticDate;
import org.threeten.extra.chrono.EthiopicChronology;
import org.threeten.extra.chrono.EthiopicDate;
import org.threeten.extra.chrono.InternationalFixedChronology;
import org.threeten.extra.chrono.InternationalFixedDate;
import org.threeten.extra.chrono.JulianChronology;
import org.threeten.extra.chrono.JulianDate;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.Chronology;
import java.time.chrono.HijrahChronology;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseChronology;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoChronology;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistChronology;
import java.time.chrono.ThaiBuddhistDate;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Builder that exposes {@link ChronoLocalDateTime} generation for any {@link Chronology}. Internally it delegates to
 * {@link ChronologyZonedDateTimeSupplierBuilder} for instant-based range handling and then drops the zone component.
 * @param <C> The chronology-specific {@link ChronoLocalDate} type embedded in the generated date-time.
 */
public class ChronologyLocalDateTimeSupplierBuilder<C extends ChronoLocalDate>
        implements ConfigurableSupplierBuilder<ChronoLocalDateTime<C>, ChronologyLocalDateTimeSupplierBuilder<C>> {

    private final ChronologyZonedDateTimeSupplierBuilder<C> zonedBuilder;

    private ChronologyLocalDateTimeSupplierBuilder(Chronology chronology, Class<C> dateType) {
        this.zonedBuilder = ChronologyZonedDateTimeSupplierBuilder.chronologyZonedDateTime(chronology, dateType);
    }

    public static ChronologyLocalDateTimeSupplierBuilder<ChronoLocalDate> chronologyDateTime(Chronology chronology) {
        return chronologyDateTime(chronology, ChronoLocalDate.class);
    }

    public static <C extends ChronoLocalDate> ChronologyLocalDateTimeSupplierBuilder<C> chronologyDateTime(
            Chronology chronology, Class<C> dateType) {
        Objects.requireNonNull(chronology, "chronology");
        Objects.requireNonNull(dateType, "dateType");
        return new ChronologyLocalDateTimeSupplierBuilder<>(chronology, dateType);
    }

    public static ChronologyLocalDateTimeSupplierBuilder<ChronoLocalDate> chronologyDateTime(String id) {
        return chronologyDateTime(ChronologyCatalog.requireChronology(id));
    }

    public static ChronologyLocalDateTimeSupplierBuilder<ThaiBuddhistDate> thaiBuddhistDateTime() {
        return chronologyDateTime(ThaiBuddhistChronology.INSTANCE, ThaiBuddhistDate.class);
    }

    public static ChronologyLocalDateTimeSupplierBuilder<JapaneseDate> japaneseDateTime() {
        return chronologyDateTime(JapaneseChronology.INSTANCE, JapaneseDate.class);
    }

    public static ChronologyLocalDateTimeSupplierBuilder<HijrahDate> hijrahDateTime() {
        return chronologyDateTime(HijrahChronology.INSTANCE, HijrahDate.class);
    }

    public static ChronologyLocalDateTimeSupplierBuilder<MinguoDate> minguoDateTime() {
        return chronologyDateTime(MinguoChronology.INSTANCE, MinguoDate.class);
    }

    public static ChronologyLocalDateTimeSupplierBuilder<CopticDate> copticDateTime() {
        return chronologyDateTime(CopticChronology.INSTANCE, CopticDate.class);
    }

    public static ChronologyLocalDateTimeSupplierBuilder<EthiopicDate> ethiopicDateTime() {
        return chronologyDateTime(EthiopicChronology.INSTANCE, EthiopicDate.class);
    }

    public static ChronologyLocalDateTimeSupplierBuilder<JulianDate> julianDateTime() {
        return chronologyDateTime(JulianChronology.INSTANCE, JulianDate.class);
    }

    public static ChronologyLocalDateTimeSupplierBuilder<InternationalFixedDate> internationalFixedDateTime() {
        return chronologyDateTime(InternationalFixedChronology.INSTANCE, InternationalFixedDate.class);
    }

    @Override
    public StreamableSupplier<ChronoLocalDateTime<C>> build() {
        StreamableSupplier<ChronoZonedDateTime<C>> zoned = zonedBuilder.build();
        return zoned.derive(ChronoZonedDateTime::toLocalDateTime);
    }

    public ChronologyLocalDateTimeSupplierBuilder<C> withZone(ZoneId zone) {
        zonedBuilder.withZone(zone);
        return this;
    }

    public ChronologyLocalDateTimeSupplierBuilder<C> instantSupplier(Supplier<Instant> supplier) {
        zonedBuilder.instantSupplier(supplier);
        return this;
    }

    public ChronologyLocalDateTimeSupplierBuilder<C> instantSupplier(InstantSupplierBuilder builder) {
        zonedBuilder.instantSupplier(builder);
        return this;
    }

    public And<ChronologyLocalDateTimeSupplierBuilder<C>, ChronoLocalDateTime<C>> between(ChronoLocalDateTime<C> start) {
        And<ChronologyZonedDateTimeSupplierBuilder<C>, ChronoLocalDateTime<C>> and = zonedBuilder.between(start);
        return end -> {
            and.and(end);
            return this;
        };
    }

    public And<ChronologyLocalDateTimeSupplierBuilder<C>, ChronoZonedDateTime<C>> between(ChronoZonedDateTime<C> start) {
        And<ChronologyZonedDateTimeSupplierBuilder<C>, ChronoZonedDateTime<C>> and = zonedBuilder.between(start);
        return end -> {
            and.and(end);
            return this;
        };
    }

    public And<ChronologyLocalDateTimeSupplierBuilder<C>, Instant> between(Instant start) {
        And<ChronologyZonedDateTimeSupplierBuilder<C>, Instant> and = zonedBuilder.between(start);
        return end -> {
            and.and(end);
            return this;
        };
    }

    public And<ChronologyLocalDateTimeSupplierBuilder<C>, ZonedDateTime> between(ZonedDateTime start) {
        And<ChronologyZonedDateTimeSupplierBuilder<C>, ZonedDateTime> and = zonedBuilder.between(start);
        return end -> {
            and.and(end);
            return this;
        };
    }

    public And<ChronologyLocalDateTimeSupplierBuilder<C>, OffsetDateTime> between(OffsetDateTime start) {
        And<ChronologyZonedDateTimeSupplierBuilder<C>, OffsetDateTime> and = zonedBuilder.between(start);
        return end -> {
            and.and(end);
            return this;
        };
    }

    public And<ChronologyLocalDateTimeSupplierBuilder<C>, Long> between(long startEpochMilli) {
        And<ChronologyZonedDateTimeSupplierBuilder<C>, Long> and = zonedBuilder.between(startEpochMilli);
        return end -> {
            and.and(end);
            return this;
        };
    }

    public And<ChronologyLocalDateTimeSupplierBuilder<C>, Long> between(Long startEpochMilli) {
        And<ChronologyZonedDateTimeSupplierBuilder<C>, Long> and = zonedBuilder.between(startEpochMilli);
        return end -> {
            and.and(end);
            return this;
        };
    }
}
