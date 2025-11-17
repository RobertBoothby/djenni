package com.robertboothby.djenni.time;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LocalDateSupplierBuilderTest {

    @Test
    public void shouldGenerateLocalDateFromInstantRange() {
        Instant start = Instant.parse("2024-01-01T00:00:00Z");
        Instant end = Instant.parse("2024-01-02T00:00:00Z");

        StreamableSupplier<LocalDate> supplier = LocalDateSupplierBuilder.aLocalDate()
                .between(start)
                .and(end)
                .withZone(ZoneId.of("UTC"))
                .build();

        assertThat(supplier.get(), is(LocalDate.of(2024, 1, 1)));
    }

    @Test
    public void shouldGenerateLocalDateFromDateRange() {
        Date start = new Date(0L);
        Date end = new Date(1_000L);

        StreamableSupplier<LocalDate> supplier = LocalDateSupplierBuilder.aLocalDate()
                .between(start)
                .and(end)
                .build();

        assertThat(supplier.get(), is(LocalDate.ofEpochDay(0)));
    }

    @Test
    public void builtLocalDateSuppliersShouldRemainStableAfterBuilderChanges() {
        LocalDateSupplierBuilder builder = LocalDateSupplierBuilder.aLocalDate()
                .between(LocalDate.of(2024, 1, 1))
                .and(LocalDate.of(2024, 1, 2));

        StreamableSupplier<LocalDate> supplier = builder.build();

        builder.between(LocalDate.of(2030, 1, 1)).and(LocalDate.of(2030, 1, 2));

        assertThat(supplier.get(), is(LocalDate.of(2024, 1, 1)));
    }
}
