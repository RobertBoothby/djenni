package com.robertboothby.djenni.time;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.lang.IntegerSupplierBuilder;
import com.robertboothby.djenni.sugar.And;
import static com.robertboothby.djenni.sugar.EasyCompare.eC;

import java.time.Period;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Builds suppliers of {@link Period}.
 */
public class PeriodSupplierBuilder implements ConfigurableSupplierBuilder<Period, PeriodSupplierBuilder> {

    private Supplier<Integer> yearsSupplier = IntegerSupplierBuilder.integerSupplier().build();
    private Supplier<Integer> monthsSupplier = IntegerSupplierBuilder.integerSupplier().build();
    private Supplier<Integer> daysSupplier = IntegerSupplierBuilder.integerSupplier().build();

    public static PeriodSupplierBuilder aPeriod() {
        return new PeriodSupplierBuilder();
    }

    @Override
    public StreamableSupplier<Period> build() {
        Supplier<Integer> years = this.yearsSupplier;
        Supplier<Integer> months = this.monthsSupplier;
        Supplier<Integer> days = this.daysSupplier;
        return () -> Period.of(years.get(), months.get(), days.get());
    }

    public PeriodSupplierBuilder yearsSupplier(Supplier<Integer> supplier) {
        this.yearsSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public PeriodSupplierBuilder monthsSupplier(Supplier<Integer> supplier) {
        this.monthsSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public PeriodSupplierBuilder daysSupplier(Supplier<Integer> supplier) {
        this.daysSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public And<PeriodSupplierBuilder, Period> between(Period minimumInclusive) {
        Objects.requireNonNull(minimumInclusive, "minimumInclusive");
        return maximumExclusive -> {
            if (!eC(PeriodComparator.periodComparator().compare(maximumExclusive, minimumInclusive))
                    .greaterThan(0)) {
                throw new IllegalArgumentException("Maximum period must be greater than minimum.");
            }
            this.yearsSupplier = IntegerSupplierBuilder.integerSupplier()
                    .between(minimumInclusive.getYears())
                    .and(maximumExclusive.getYears())
                    .build();
            this.monthsSupplier = IntegerSupplierBuilder.integerSupplier()
                    .between(minimumInclusive.getMonths())
                    .and(maximumExclusive.getMonths())
                    .build();
            this.daysSupplier = IntegerSupplierBuilder.integerSupplier()
                    .between(minimumInclusive.getDays())
                    .and(maximumExclusive.getDays())
                    .build();
            return this;
        };
    }

}
