package com.robertboothby.djenni.time;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class InstantSupplierBuilderTest {

    @Test
    public void shouldGenerateInstantWithinInstantRange() {
        Instant start = Instant.ofEpochMilli(1_000L);
        Instant end = Instant.ofEpochMilli(1_000L + 1);

        StreamableSupplier<Instant> supplier = InstantSupplierBuilder.anInstant()
                .between(start)
                .and(end)
                .build();

        assertThat(supplier.get(), is(start));
    }

    @Test
    public void shouldGenerateInstantWithinDateRange() {
        Date start = new Date(2_000L);
        Date end = new Date(2_000L + 1);

        StreamableSupplier<Instant> supplier = InstantSupplierBuilder.anInstant()
                .between(start)
                .and(end)
                .build();

        assertThat(supplier.get(), is(Instant.ofEpochMilli(2_000L)));
    }

    @Test
    public void shouldGenerateInstantWithinLocalDateRangeUsingZone() {
        ZoneId zone = ZoneId.of("UTC");
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = start.plusDays(1);

        StreamableSupplier<Instant> supplier = InstantSupplierBuilder.anInstant()
                .withZone(zone)
                .between(start)
                .and(end)
                .build();

        Instant generated = supplier.get();
        assertThat(!generated.isBefore(start.atStartOfDay(zone).toInstant())
                && generated.isBefore(end.atStartOfDay(zone).toInstant()), is(true));
    }

    @Test
    public void shouldGenerateInstantWithinLocalDateTimeRangeUsingZone() {
        ZoneId zone = ZoneId.of("UTC");
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 12, 0);
        LocalDateTime end = start.plusNanos(1_000_000);

        StreamableSupplier<Instant> supplier = InstantSupplierBuilder.anInstant()
                .withZone(zone)
                .between(start)
                .and(end)
                .build();

        assertThat(supplier.get(), is(start.atZone(zone).toInstant()));
    }

    @Test
    public void shouldGenerateInstantWithinTimestampRange() {
        Timestamp start = new Timestamp(3_000L);
        Timestamp end = new Timestamp(3_000L + 1);

        StreamableSupplier<Instant> supplier = InstantSupplierBuilder.anInstant()
                .between(start)
                .and(end)
                .build();

        assertThat(supplier.get(), is(Instant.ofEpochMilli(3_000L)));
    }

    @Test
    public void shouldGenerateInstantWithinCalendarRange() {
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(4_000L);
        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(4_000L + 1);

        StreamableSupplier<Instant> supplier = InstantSupplierBuilder.anInstant()
                .between(start)
                .and(end)
                .build();

        assertThat(supplier.get(), is(Instant.ofEpochMilli(4_000L)));
    }

    @Test
    public void builtInstantSuppliersShouldBeUnaffectedByBuilderChanges() {
        Instant fixedInstant = Instant.ofEpochSecond(10);
        InstantSupplierBuilder builder = InstantSupplierBuilder.anInstant()
                .epochMilliSupplier(() -> fixedInstant.toEpochMilli());

        StreamableSupplier<Instant> supplier = builder.build();

        builder.between(Instant.ofEpochSecond(20)).and(Instant.ofEpochSecond(21));

        assertThat(supplier.get(), is(fixedInstant));
    }
}
