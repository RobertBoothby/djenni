package com.robertboothby.djenni.time;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.lang.LongSupplierBuilder;
import com.robertboothby.djenni.sugar.And;
import org.threeten.extra.chrono.CopticChronology;
import org.threeten.extra.chrono.CopticDate;
import org.threeten.extra.chrono.EthiopicChronology;
import org.threeten.extra.chrono.EthiopicDate;
import org.threeten.extra.chrono.JulianChronology;
import org.threeten.extra.chrono.JulianDate;
import org.threeten.extra.chrono.InternationalFixedChronology;
import org.threeten.extra.chrono.InternationalFixedDate;

import java.time.DateTimeException;
import java.time.chrono.ChronoLocalDate;
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
 * Builder that produces {@link ChronoLocalDate} instances backed by any {@link Chronology} visible on the classpath.
 * This makes it possible to generate calendar-aware data for Japanese, Thai Buddhist, Hijrah, and other international
 * calendar systems, including the additional chronologies shipped with ThreeTen-Extra.
 * @param <C> The concrete chronology-backed date type emitted by the builder.
 */
public class ChronologyLocalDateSupplierBuilder<C extends ChronoLocalDate>
        implements ConfigurableSupplierBuilder<C, ChronologyLocalDateSupplierBuilder<C>> {

    private final Chronology chronology;
    private final Class<C> dateType;
    private Supplier<Long> epochDaySupplier;

    private ChronologyLocalDateSupplierBuilder(Chronology chronology, Class<C> dateType) {
        this.chronology = Objects.requireNonNull(chronology, "chronology");
        this.dateType = Objects.requireNonNull(dateType, "dateType");
        this.epochDaySupplier = LongSupplierBuilder.generateALong()
                .onlyValue(this.chronology.dateNow().toEpochDay())
                .build();
    }

    public static ChronologyLocalDateSupplierBuilder<ChronoLocalDate> chronologyDate(Chronology chronology) {
        return chronologyDate(chronology, ChronoLocalDate.class);
    }

    public static <C extends ChronoLocalDate> ChronologyLocalDateSupplierBuilder<C> chronologyDate(
            Chronology chronology, Class<C> dateType) {
        return new ChronologyLocalDateSupplierBuilder<>(chronology, dateType);
    }

    public static ChronologyLocalDateSupplierBuilder<ChronoLocalDate> chronologyById(String chronologyId) {
        return chronologyDate(ChronologyCatalog.requireChronology(chronologyId));
    }

    public static ChronologyLocalDateSupplierBuilder<ThaiBuddhistDate> thaiBuddhistDate() {
        return chronologyDate(ThaiBuddhistChronology.INSTANCE, ThaiBuddhistDate.class);
    }

    public static ChronologyLocalDateSupplierBuilder<JapaneseDate> japaneseDate() {
        return chronologyDate(JapaneseChronology.INSTANCE, JapaneseDate.class);
    }

    public static ChronologyLocalDateSupplierBuilder<HijrahDate> hijrahDate() {
        return chronologyDate(HijrahChronology.INSTANCE, HijrahDate.class);
    }

    public static ChronologyLocalDateSupplierBuilder<MinguoDate> minguoDate() {
        return chronologyDate(MinguoChronology.INSTANCE, MinguoDate.class);
    }

    public static ChronologyLocalDateSupplierBuilder<CopticDate> copticDate() {
        return chronologyDate(CopticChronology.INSTANCE, CopticDate.class);
    }

    public static ChronologyLocalDateSupplierBuilder<EthiopicDate> ethiopicDate() {
        return chronologyDate(EthiopicChronology.INSTANCE, EthiopicDate.class);
    }

    public static ChronologyLocalDateSupplierBuilder<JulianDate> julianDate() {
        return chronologyDate(JulianChronology.INSTANCE, JulianDate.class);
    }

    public static ChronologyLocalDateSupplierBuilder<InternationalFixedDate> internationalFixedDate() {
        return chronologyDate(InternationalFixedChronology.INSTANCE, InternationalFixedDate.class);
    }

    @Override
    public StreamableSupplier<C> build() {
        Supplier<Long> epochDays = this.epochDaySupplier;
        Chronology chronology = this.chronology;
        Class<C> targetType = this.dateType;
        return () -> {
            ChronoLocalDate date = chronology.dateEpochDay(epochDays.get());
            if (!targetType.isInstance(date)) {
                throw new DateTimeException("Chronology " + chronology.getId()
                        + " produced unexpected type " + date.getClass().getName());
            }
            return targetType.cast(date);
        };
    }

    public And<ChronologyLocalDateSupplierBuilder<C>, C> between(C startInclusive) {
        Objects.requireNonNull(startInclusive, "startInclusive");
        requireChronologyMatch(startInclusive);
        long startEpochDay = startInclusive.toEpochDay();
        return endExclusive -> {
            Objects.requireNonNull(endExclusive, "endExclusive");
            requireChronologyMatch(endExclusive);
            long endEpochDay = endExclusive.toEpochDay();
            if (endEpochDay <= startEpochDay) {
                throw new IllegalArgumentException("End date must be after the start date.");
            }
            this.epochDaySupplier = LongSupplierBuilder.generateALong()
                    .between(startEpochDay)
                    .and(endEpochDay)
                    .build();
            return this;
        };
    }

    private void requireChronologyMatch(C date) {
        Chronology dateChronology = date.getChronology();
        if (!chronology.equals(dateChronology)) {
            throw new IllegalArgumentException("Expected chronology " + chronology.getId()
                    + " but found " + dateChronology.getId());
        }
    }
}
