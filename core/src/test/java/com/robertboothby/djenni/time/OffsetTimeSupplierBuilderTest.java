package com.robertboothby.djenni.time;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.OffsetTime;
import java.time.ZoneOffset;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class OffsetTimeSupplierBuilderTest {

    @Test
    public void shouldGenerateOffsetTimeInFixedOffsetRange() {
        ZoneOffset offset = ZoneOffset.UTC;
        Instant start = Instant.ofEpochMilli(10_000);
        Instant end = start.plusMillis(1);

        StreamableSupplier<OffsetTime> supplier = OffsetTimeSupplierBuilder.anOffsetTime()
                .fixedOffset(offset)
                .between(start)
                .and(end)
                .build();

        assertThat(supplier.get(), is(OffsetTime.ofInstant(start, offset)));
    }

    @Test
    public void builtOffsetTimeSuppliersShouldRemainStableAfterBuilderChanges() {
        Instant fixedInstant = Instant.ofEpochSecond(5);
        OffsetTimeSupplierBuilder builder = OffsetTimeSupplierBuilder.anOffsetTime()
                .fixedOffset(ZoneOffset.UTC)
                .between(Instant.ofEpochSecond(5))
                .and(Instant.ofEpochSecond(6));

        builder.instantSupplier(() -> fixedInstant); // ensure deterministic

        StreamableSupplier<OffsetTime> supplier = builder.build();

        builder.between(Instant.ofEpochSecond(10)).and(Instant.ofEpochSecond(11));

        assertThat(supplier.get(), is(OffsetTime.ofInstant(fixedInstant, ZoneOffset.UTC)));
    }
}
