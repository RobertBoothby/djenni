package com.robertboothby.djenni.util;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.Test;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CalendarSupplierBuilderTest {

    @Test
    public void shouldUseDateAndZoneSuppliers() {
        Date date = new Date(10_000L);
        ZoneId zone = ZoneId.of("UTC");

        StreamableSupplier<Calendar> calendars = CalendarSupplierBuilder.calendarSupplier()
                .dateSupplier(() -> date)
                .zoneSupplier(() -> zone)
                .build();

        Calendar calendar = calendars.get();
        assertThat(calendar.getTime(), is(date));
        assertThat(calendar.getTimeZone().toZoneId(), is(zone));
    }

    @Test
    public void builtCalendarSuppliersShouldRemainStableAfterBuilderChanges() {
        CalendarSupplierBuilder builder = CalendarSupplierBuilder.calendarSupplier()
                .dateSupplier(() -> new Date(0L));

        StreamableSupplier<Calendar> supplier = builder.build();

        builder.dateSupplier(() -> new Date(1_000L));

        assertThat(supplier.get().getTime(), is(new Date(0L)));
    }
}
