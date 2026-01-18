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

import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.OffsetDateTime;
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

import static com.robertboothby.djenni.time.InstantSupplierBuilder.anInstant;

/**
 * Builder that produces {@link ChronoZonedDateTime} values for any {@link Chronology} available to the JVM, including
 * the ThreeTen-Extra calendar implementations. Ranges are expressed in terms of {@link Instant} values to retain
 * interop with existing builders.
 * @param <C> The chronology-specific {@link ChronoLocalDate} component embedded in the zoned date-time.
 */
public class ChronologyZonedDateTimeSupplierBuilder<C extends ChronoLocalDate>
        implements ConfigurableSupplierBuilder<ChronoZonedDateTime<C>, ChronologyZonedDateTimeSupplierBuilder<C>> {

    private final Chronology chronology;
    private final Class<C> dateType;
    private Supplier<Instant> instantSupplier = anInstant().build();
    private ZoneId zone = ZoneId.systemDefault();

    private ChronologyZonedDateTimeSupplierBuilder(Chronology chronology, Class<C> dateType) {
        this.chronology = Objects.requireNonNull(chronology, "chronology");
        this.dateType = Objects.requireNonNull(dateType, "dateType");
    }

    public static ChronologyZonedDateTimeSupplierBuilder<ChronoLocalDate> chronologyZonedDateTime(Chronology chronology) {
        return chronologyZonedDateTime(chronology, ChronoLocalDate.class);
    }

    public static <C extends ChronoLocalDate> ChronologyZonedDateTimeSupplierBuilder<C> chronologyZonedDateTime(
            Chronology chronology, Class<C> dateType) {
        return new ChronologyZonedDateTimeSupplierBuilder<>(chronology, dateType);
    }

    public static ChronologyZonedDateTimeSupplierBuilder<ChronoLocalDate> chronologyZonedDateTime(String id) {
        return chronologyZonedDateTime(ChronologyCatalog.requireChronology(id));
    }

    public static ChronologyZonedDateTimeSupplierBuilder<ThaiBuddhistDate> thaiBuddhistZonedDateTime() {
        return chronologyZonedDateTime(ThaiBuddhistChronology.INSTANCE, ThaiBuddhistDate.class);
    }

    public static ChronologyZonedDateTimeSupplierBuilder<JapaneseDate> japaneseZonedDateTime() {
        return chronologyZonedDateTime(JapaneseChronology.INSTANCE, JapaneseDate.class);
    }

    public static ChronologyZonedDateTimeSupplierBuilder<HijrahDate> hijrahZonedDateTime() {
        return chronologyZonedDateTime(HijrahChronology.INSTANCE, HijrahDate.class);
    }

    public static ChronologyZonedDateTimeSupplierBuilder<MinguoDate> minguoZonedDateTime() {
        return chronologyZonedDateTime(MinguoChronology.INSTANCE, MinguoDate.class);
    }

    public static ChronologyZonedDateTimeSupplierBuilder<CopticDate> copticZonedDateTime() {
        return chronologyZonedDateTime(CopticChronology.INSTANCE, CopticDate.class);
    }

    public static ChronologyZonedDateTimeSupplierBuilder<EthiopicDate> ethiopicZonedDateTime() {
        return chronologyZonedDateTime(EthiopicChronology.INSTANCE, EthiopicDate.class);
    }

    public static ChronologyZonedDateTimeSupplierBuilder<JulianDate> julianZonedDateTime() {
        return chronologyZonedDateTime(JulianChronology.INSTANCE, JulianDate.class);
    }

    public static ChronologyZonedDateTimeSupplierBuilder<InternationalFixedDate> internationalFixedZonedDateTime() {
        return chronologyZonedDateTime(InternationalFixedChronology.INSTANCE, InternationalFixedDate.class);
    }

    @Override
    public StreamableSupplier<ChronoZonedDateTime<C>> build() {
        Supplier<Instant> instants = this.instantSupplier;
        Chronology chronology = this.chronology;
        ZoneId zone = this.zone;
        Class<C> dateType = this.dateType;
        return () -> {
            ChronoZonedDateTime<? extends ChronoLocalDate> zonedDateTime = chronology.zonedDateTime(instants.get(), zone);
            ChronoLocalDate date = zonedDateTime.toLocalDate();
            if (!dateType.isInstance(date)) {
                throw new DateTimeException("Chronology " + chronology.getId()
                        + " produced unexpected date type " + date.getClass().getName());
            }
            @SuppressWarnings("unchecked")
            ChronoZonedDateTime<C> cast = (ChronoZonedDateTime<C>) zonedDateTime;
            return cast;
        };
    }

    public ChronologyZonedDateTimeSupplierBuilder<C> withZone(ZoneId zone) {
        this.zone = Objects.requireNonNull(zone, "zone");
        return this;
    }

    public ChronologyZonedDateTimeSupplierBuilder<C> instantSupplier(Supplier<Instant> supplier) {
        this.instantSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public ChronologyZonedDateTimeSupplierBuilder<C> instantSupplier(InstantSupplierBuilder builder) {
        Objects.requireNonNull(builder, "builder");
        return instantSupplier(builder.build());
    }

    ZoneId zone() {
        return zone;
    }

    public And<ChronologyZonedDateTimeSupplierBuilder<C>, ChronoZonedDateTime<C>> between(ChronoZonedDateTime<C> start) {
        Objects.requireNonNull(start, "start");
        requireChronologyMatch(start);
        Instant startInstant = start.toInstant();
        return end -> {
            Objects.requireNonNull(end, "end");
            requireChronologyMatch(end);
            return configureRange(startInstant, end.toInstant());
        };
    }

    public And<ChronologyZonedDateTimeSupplierBuilder<C>, Instant> between(Instant start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<ChronologyZonedDateTimeSupplierBuilder<C>, ChronoLocalDateTime<C>> between(ChronoLocalDateTime<C> start) {
        Objects.requireNonNull(start, "start");
        ChronoZonedDateTime<C> zoned = start.atZone(zone);
        And<ChronologyZonedDateTimeSupplierBuilder<C>, ChronoZonedDateTime<C>> and = between(zoned);
        return end -> {
            Objects.requireNonNull(end, "end");
            and.and(end.atZone(zone));
            return this;
        };
    }

    public And<ChronologyZonedDateTimeSupplierBuilder<C>, ZonedDateTime> between(ZonedDateTime start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<ChronologyZonedDateTimeSupplierBuilder<C>, OffsetDateTime> between(OffsetDateTime start) {
        Instant startInstant = TimeConversions.toInstant(start);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<ChronologyZonedDateTimeSupplierBuilder<C>, Long> between(long startEpochMilli) {
        Instant startInstant = TimeConversions.toInstant(startEpochMilli);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    public And<ChronologyZonedDateTimeSupplierBuilder<C>, Long> between(Long startEpochMilli) {
        Instant startInstant = TimeConversions.toInstant(startEpochMilli);
        return end -> configureRange(startInstant, TimeConversions.toInstant(end));
    }

    private ChronologyZonedDateTimeSupplierBuilder<C> configureRange(Instant startInclusive, Instant endExclusive) {
        if (!endExclusive.isAfter(startInclusive)) {
            throw new IllegalArgumentException("End instant must be after start instant.");
        }
        this.instantSupplier = anInstant().between(startInclusive).and(endExclusive).build();
        return this;
    }

    private void requireChronologyMatch(ChronoZonedDateTime<C> dateTime) {
        Chronology chronology = dateTime.getChronology();
        if (!this.chronology.equals(chronology)) {
            throw new IllegalArgumentException("Expected chronology " + this.chronology.getId()
                    + " but found " + chronology.getId());
        }
    }
}
