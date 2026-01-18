package com.robertboothby.djenni.time;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LocalDateTimeSupplierBuilderTest {

    @Test
    public void shouldGenerateLocalDateTimeFromInstantRange() {
        Instant start = Instant.parse("2024-01-01T00:00:00Z");
        Instant end = start.plusMillis(1);

        StreamableSupplier<LocalDateTime> supplier = LocalDateTimeSupplierBuilder.aLocalDateTime()
                .withZone(ZoneId.of("UTC"))
                .between(start)
                .and(end)
                .build();

        assertThat(supplier.get(), is(LocalDateTime.of(2024, 1, 1, 0, 0)));
    }

    @Test
    public void builtLocalDateTimeSuppliersShouldRemainStableAfterBuilderChanges() {
        Instant fixedInstant = Instant.parse("2024-01-01T00:00:00Z");
        LocalDateTimeSupplierBuilder builder = LocalDateTimeSupplierBuilder.aLocalDateTime()
                .withZone(ZoneId.of("UTC"))
                .instantSupplier(() -> fixedInstant);

        StreamableSupplier<LocalDateTime> supplier = builder.build();

        builder.between(Instant.parse("2030-01-01T00:00:00Z")).and(Instant.parse("2030-01-01T00:00:01Z"));

        assertThat(supplier.get(), is(LocalDateTime.of(2024, 1, 1, 0, 0)));
    }
}
