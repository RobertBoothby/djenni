package com.robertboothby.djenni.util;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.Test;

import java.time.Instant;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DateSupplierBuilderTest {

    @Test
    public void shouldConvertInstantSupplierToDate() {
        StreamableSupplier<Date> dates = DateSupplierBuilder.dateSupplier()
                .instantSupplier(() -> Instant.ofEpochMilli(1234L))
                .build();

        assertThat(dates.get(), is(new Date(1234L)));
    }

    @Test
    public void shouldGenerateDatesBetweenInstants() {
        Instant start = Instant.ofEpochMilli(5000L);
        Instant end = Instant.ofEpochMilli(5001L);

        StreamableSupplier<Date> dates = DateSupplierBuilder.dateSupplier()
                .between(start)
                .and(end)
                .build();

        assertThat(dates.get(), is(new Date(5000L)));
    }
}
