package com.robertboothby.djenni.time;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.Test;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class OffsetDateTimeSupplierBuilderTest {

    @Test
    public void shouldGenerateOffsetDateTimeInFixedOffsetRange() {
        ZoneOffset offset = ZoneOffset.ofHours(2);
        Instant start = Instant.ofEpochMilli(1000);
        Instant end = start.plusMillis(1);

        StreamableSupplier<OffsetDateTime> supplier = OffsetDateTimeSupplierBuilder.anOffsetDateTime()
                .fixedOffset(offset)
                .between(start)
                .and(end)
                .build();

        assertThat(supplier.get(), is(OffsetDateTime.ofInstant(start, offset)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectInvalidRange() {
        OffsetDateTimeSupplierBuilder.anOffsetDateTime()
                .between(Instant.ofEpochMilli(5))
                .and(Instant.ofEpochMilli(5));
    }

    @Test
    public void builtOffsetDateTimeSuppliersShouldRemainStableAfterBuilderChanges() {
        Instant fixedInstant = Instant.ofEpochSecond(10);
        OffsetDateTimeSupplierBuilder builder = OffsetDateTimeSupplierBuilder.anOffsetDateTime()
                .fixedOffset(ZoneOffset.UTC)
                .instantSupplier(() -> fixedInstant);

        StreamableSupplier<OffsetDateTime> supplier = builder.build();

        builder.between(Instant.ofEpochSecond(20)).and(Instant.ofEpochSecond(21));

        assertThat(supplier.get(), is(OffsetDateTime.ofInstant(fixedInstant, ZoneOffset.UTC)));
    }
}
