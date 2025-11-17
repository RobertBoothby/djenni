package com.robertboothby.djenni.time;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.Test;

import java.time.MonthDay;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MonthDaySupplierBuilderTest {

    @Test
    public void shouldGenerateMonthDayWithinRange() {
        MonthDay start = MonthDay.of(1, 1);
        MonthDay end = MonthDay.of(1, 2);

        StreamableSupplier<MonthDay> supplier = MonthDaySupplierBuilder.aMonthDay()
                .between(start)
                .and(end)
                .build();

        assertThat(supplier.get(), is(start));
    }

    @Test
    public void builtMonthDaySuppliersShouldRemainStableAfterBuilderChanges() {
        MonthDaySupplierBuilder builder = MonthDaySupplierBuilder.aMonthDay()
                .monthSupplier(() -> 1)
                .daySupplier(() -> 1);

        StreamableSupplier<MonthDay> supplier = builder.build();

        builder.daySupplier(() -> 2);

        assertThat(supplier.get(), is(MonthDay.of(1, 1)));
    }
}
