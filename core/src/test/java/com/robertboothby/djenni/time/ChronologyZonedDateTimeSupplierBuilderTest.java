package com.robertboothby.djenni.time;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.Test;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.ThaiBuddhistDate;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ChronologyZonedDateTimeSupplierBuilderTest {

    @Test
    public void shouldGenerateThaiBuddhistZonedDateTimeWithinRange() {
        ZoneId zone = ZoneId.of("Asia/Bangkok");
        ChronoLocalDateTime<ThaiBuddhistDate> startLocal = ThaiBuddhistDate.of(2567, 1, 1).atTime(LocalTime.NOON);
        ChronoZonedDateTime<ThaiBuddhistDate> start = startLocal.atZone(zone);
        ChronoZonedDateTime<ThaiBuddhistDate> end = start.plus(1, ChronoUnit.DAYS);

        StreamableSupplier<ChronoZonedDateTime<ThaiBuddhistDate>> supplier =
                ChronologyZonedDateTimeSupplierBuilder.thaiBuddhistZonedDateTime()
                        .withZone(zone)
                        .instantSupplier(() -> start.toInstant())
                        .between(start)
                        .and(end)
                        .build();

        ChronoZonedDateTime<ThaiBuddhistDate> value = supplier.get();
        assertThat(value.getChronology(), is(start.getChronology()));
        assertThat(value.toInstant().isBefore(end.toInstant()), is(true));
        assertThat(value.toInstant().isAfter(start.toInstant()) || value.toInstant().equals(start.toInstant()), is(true));
    }

    @Test
    public void shouldGenerateThaiBuddhistLocalDateTimeWithinRange() {
        ZoneId zone = ZoneId.of("Asia/Bangkok");
        ChronoLocalDateTime<ThaiBuddhistDate> start = ThaiBuddhistDate.of(2567, 1, 1).atTime(LocalTime.NOON);
        ChronoLocalDateTime<ThaiBuddhistDate> end = start.plus(1, ChronoUnit.DAYS);

        StreamableSupplier<ChronoLocalDateTime<ThaiBuddhistDate>> supplier =
                ChronologyLocalDateTimeSupplierBuilder.thaiBuddhistDateTime()
                        .withZone(zone)
                        .instantSupplier(() -> start.atZone(zone).toInstant())
                        .between(start)
                        .and(end)
                        .build();

        ChronoLocalDateTime<ThaiBuddhistDate> value = supplier.get();
        assertThat(value.getChronology(), is(start.getChronology()));
        assertThat(value.isBefore(end), is(true));
        assertThat(!value.isBefore(start), is(true));
    }
}
