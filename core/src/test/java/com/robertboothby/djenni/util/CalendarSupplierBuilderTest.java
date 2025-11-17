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
}
