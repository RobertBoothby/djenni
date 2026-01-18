package com.robertboothby.djenni.time;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.jupiter.api.Test;

import java.time.Period;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PeriodSupplierBuilderTest {

    @Test
    public void shouldGeneratePeriodWithinRange() {
        Period start = Period.of(1, 2, 3);
        Period end = Period.of(2, 3, 4);

        StreamableSupplier<Period> supplier = PeriodSupplierBuilder.aPeriod()
                .between(start)
                .and(end)
                .build();

        assertThat(supplier.get(), is(start));
    }

    @Test
    public void shouldRejectInvalidRange() {
        assertThrows(IllegalArgumentException.class, () -> PeriodSupplierBuilder.aPeriod()
                .between(Period.ZERO)
                .and(Period.ZERO));
    }

    @Test
    public void builtPeriodSuppliersShouldRemainStableAfterBuilderChanges() {
        PeriodSupplierBuilder builder = PeriodSupplierBuilder.aPeriod()
                .yearsSupplier(() -> 1)
                .monthsSupplier(() -> 2)
                .daysSupplier(() -> 3);

        StreamableSupplier<Period> supplier = builder.build();

        builder.yearsSupplier(() -> 4);

        assertThat(supplier.get(), is(Period.of(1, 2, 3)));
    }
}
