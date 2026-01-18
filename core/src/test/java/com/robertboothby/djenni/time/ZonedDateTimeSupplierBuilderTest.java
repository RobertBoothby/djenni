package com.robertboothby.djenni.time;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ZonedDateTimeSupplierBuilderTest {

    @Test
    public void shouldGenerateZonedDateTimeWithFixedZone() {
        ZoneId zone = ZoneId.of("UTC");
        Instant start = Instant.ofEpochMilli(0L);
        Instant end = start.plusMillis(1);

        StreamableSupplier<ZonedDateTime> supplier = ZonedDateTimeSupplierBuilder.aZonedDateTime()
                .fixedZone(zone)
                .between(start)
                .and(end)
                .build();

        assertThat(supplier.get(), is(ZonedDateTime.ofInstant(start, zone)));
    }

    @Test
    public void shouldGenerateZonedDateTimeWithZoneSupplier() {
        ZoneId zone = ZoneId.of("Europe/Paris");
        StreamableSupplier<ZonedDateTime> supplier = ZonedDateTimeSupplierBuilder.aZonedDateTime()
                .zoneSupplier(() -> zone)
                .between(Instant.ofEpochMilli(5_000L))
                .and(Instant.ofEpochMilli(5_100L))
                .build();

        assertThat(supplier.get().getZone(), is(zone));
    }

    @Test
    public void builtZonedDateTimeSuppliersShouldRemainStableAfterBuilderChanges() {
        Instant fixedInstant = Instant.ofEpochSecond(10);
        ZonedDateTimeSupplierBuilder builder = ZonedDateTimeSupplierBuilder.aZonedDateTime()
                .fixedZone(ZoneId.of("UTC"))
                .instantSupplier(() -> fixedInstant);

        StreamableSupplier<ZonedDateTime> supplier = builder.build();

        builder.fixedZone(ZoneId.of("Europe/Paris"));

        assertThat(supplier.get(), is(ZonedDateTime.ofInstant(fixedInstant, ZoneId.of("UTC"))));
    }
}
