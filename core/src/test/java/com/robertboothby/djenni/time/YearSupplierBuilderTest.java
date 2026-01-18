package com.robertboothby.djenni.time;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class YearSupplierBuilderTest {

    @Test
    public void shouldGenerateYearWithinRange() {
        Year start = Year.of(2000);
        Year end = Year.of(2001);

        StreamableSupplier<Year> supplier = YearSupplierBuilder.aYear()
                .between(start)
                .and(end)
                .build();

        assertThat(supplier.get(), is(start));
    }

    @Test
    public void builtYearSuppliersShouldRemainStableAfterBuilderChanges() {
        YearSupplierBuilder builder = YearSupplierBuilder.aYear()
                .yearSupplier(() -> 2000);

        StreamableSupplier<Year> supplier = builder.build();

        builder.yearSupplier(() -> 2020);

        assertThat(supplier.get(), is(Year.of(2000)));
    }
}
