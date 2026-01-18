package com.robertboothby.djenni.time;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class YearMonthSupplierBuilderTest {

    @Test
    public void shouldGenerateYearMonthWithinRange() {
        YearMonth start = YearMonth.of(2020, 1);
        YearMonth end = start.plusMonths(1);

        StreamableSupplier<YearMonth> supplier = YearMonthSupplierBuilder.aYearMonth()
                .between(start)
                .and(end)
                .build();

        assertThat(supplier.get(), is(start));
    }

    @Test
    public void builtYearMonthSuppliersShouldRemainStableAfterBuilderChanges() {
        YearMonthSupplierBuilder builder = YearMonthSupplierBuilder.aYearMonth()
                .yearSupplier(() -> 2020)
                .monthSupplier(() -> 1);

        StreamableSupplier<YearMonth> supplier = builder.build();

        builder.yearSupplier(() -> 2030);

        assertThat(supplier.get(), is(YearMonth.of(2020, 1)));
    }
}
