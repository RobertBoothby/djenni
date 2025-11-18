package com.robertboothby.djenni.time;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.lang.IntegerSupplierBuilder;
import com.robertboothby.djenni.sugar.And;

import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Builds suppliers of {@link YearMonth}.
 */
public class YearMonthSupplierBuilder implements ConfigurableSupplierBuilder<YearMonth, YearMonthSupplierBuilder> {

    private Supplier<Integer> yearSupplier = IntegerSupplierBuilder.integerSupplier().build();
    private Supplier<Integer> monthSupplier = IntegerSupplierBuilder.integerSupplier()
            .between(1)
            .and(13)
            .build();
    private StreamableSupplier<YearMonth> rangeSupplier;

    public static YearMonthSupplierBuilder aYearMonth() {
        return new YearMonthSupplierBuilder();
    }

    @Override
    public StreamableSupplier<YearMonth> build() {
        StreamableSupplier<YearMonth> rangeSupplier = this.rangeSupplier;
        if (rangeSupplier != null) {
            return rangeSupplier;
        }
        Supplier<Integer> years = this.yearSupplier;
        Supplier<Integer> months = this.monthSupplier;
        return () -> YearMonth.of(years.get(), months.get());
    }

    public YearMonthSupplierBuilder yearSupplier(Supplier<Integer> supplier) {
        this.rangeSupplier = null;
        this.yearSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public YearMonthSupplierBuilder monthSupplier(Supplier<Integer> supplier) {
        this.rangeSupplier = null;
        this.monthSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public And<YearMonthSupplierBuilder, YearMonth> between(YearMonth startInclusive) {
        Objects.requireNonNull(startInclusive, "startInclusive");
        return endExclusive -> {
            if (!endExclusive.isAfter(startInclusive)) {
                throw new IllegalArgumentException("End YearMonth must be after start YearMonth.");
            }
            long monthSpan = ChronoUnit.MONTHS.between(startInclusive, endExclusive);
            if (monthSpan > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("Range too large to represent with integer offsets.");
            }
            StreamableSupplier<Integer> offsets = IntegerSupplierBuilder.integerSupplier()
                    .between(0)
                    .and(Math.toIntExact(monthSpan))
                    .build();
            this.rangeSupplier = offsets.derive(startInclusive::plusMonths);
            return this;
        };
    }
}
