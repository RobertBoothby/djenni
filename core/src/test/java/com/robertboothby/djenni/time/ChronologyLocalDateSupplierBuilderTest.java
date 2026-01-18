package com.robertboothby.djenni.time;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.jupiter.api.Test;
import org.threeten.extra.chrono.CopticChronology;
import org.threeten.extra.chrono.CopticDate;
import org.threeten.extra.chrono.JulianChronology;
import org.threeten.extra.chrono.JulianDate;

import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ThaiBuddhistChronology;
import java.time.chrono.ThaiBuddhistDate;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ChronologyLocalDateSupplierBuilderTest {

    @Test
    public void shouldGenerateThaiBuddhistDateWithinRange() {
        ThaiBuddhistDate start = ThaiBuddhistDate.of(2567, 1, 1);
        ThaiBuddhistDate end = start.plus(1, ChronoUnit.DAYS);

        StreamableSupplier<ThaiBuddhistDate> supplier = ChronologyLocalDateSupplierBuilder.thaiBuddhistDate()
                .between(start)
                .and(end)
                .build();

        assertThat(supplier.get(), is(start));
    }

    @Test
    public void shouldRejectMismatchedChronology() {
        ThaiBuddhistDate start = ThaiBuddhistDate.of(2567, 1, 1);
        ChronologyLocalDateSupplierBuilder<ChronoLocalDate> builder =
                ChronologyLocalDateSupplierBuilder.chronologyDate(ThaiBuddhistChronology.INSTANCE, ChronoLocalDate.class);
        assertThrows(IllegalArgumentException.class, () -> builder.between(start)
                .and(CopticChronology.INSTANCE.dateEpochDay(start.toEpochDay())));
    }

    @Test
    public void shouldExposeThreeTenExtraChronologies() {
        ChronologyLocalDateSupplierBuilder<ChronoLocalDate> builder =
                ChronologyLocalDateSupplierBuilder.chronologyById(JulianChronology.INSTANCE.getId());

        ChronoLocalDate start = JulianDate.of(2024, 1, 1);
        ChronoLocalDate end = JulianDate.of(2024, 1, 2);

        StreamableSupplier<ChronoLocalDate> supplier = builder
                .between(start)
                .and(end)
                .build();

        assertThat(supplier.get(), is(start));
    }

    @Test
    public void shouldEnumerateChronologyIdsIncludingCoptic() {
        assertThat(ChronologyCatalog.availableChronologyIds(), hasItem(CopticChronology.INSTANCE.getId()));
        assertThat(ChronologyCatalog.availableChronologyIds(), not(hasItem("does-not-exist")));
    }
}
