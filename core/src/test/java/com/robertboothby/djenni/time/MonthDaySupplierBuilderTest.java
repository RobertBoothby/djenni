package com.robertboothby.djenni.time;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.jupiter.api.Test;

import java.time.MonthDay;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    public void shouldIncludeLeapDayByDefault() {
        StreamableSupplier<MonthDay> supplier = MonthDaySupplierBuilder.aMonthDay()
                .between(MonthDay.of(2, 29))
                .and(MonthDay.of(3, 1))
                .build();

        assertThat(supplier.get(), is(MonthDay.of(2, 29)));
    }

    @Test
    public void shouldPreventLeapDayGenerationWhenConfigured() {
        StreamableSupplier<MonthDay> supplier = MonthDaySupplierBuilder.aMonthDay()
                .preventLeapDay(true)
                .between(MonthDay.of(2, 28))
                .and(MonthDay.of(3, 2))
                .build();

        boolean leapDayGenerated = supplier.stream(32).anyMatch(monthDay -> monthDay.equals(MonthDay.of(2, 29)));
        assertThat(leapDayGenerated, is(false));
    }

    @Test
    public void preventLeapDayShouldRejectLeapDayOnlyRange() {
        assertThrows(IllegalArgumentException.class, () -> MonthDaySupplierBuilder.aMonthDay()
                .preventLeapDay(true)
                .between(MonthDay.of(2, 29))
                .and(MonthDay.of(3, 1))
                .build());
    }
}
