package com.robertboothby.djenni.time;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.lang.IntegerSupplierBuilder;
import com.robertboothby.djenni.sugar.And;

import java.time.YearMonth;
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

    public static YearMonthSupplierBuilder aYearMonth() {
        return new YearMonthSupplierBuilder();
    }

    @Override
    public StreamableSupplier<YearMonth> build() {
        Supplier<Integer> years = this.yearSupplier;
        Supplier<Integer> months = this.monthSupplier;
        return () -> YearMonth.of(years.get(), months.get());
    }

    public YearMonthSupplierBuilder yearSupplier(Supplier<Integer> supplier) {
        this.yearSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public YearMonthSupplierBuilder monthSupplier(Supplier<Integer> supplier) {
        this.monthSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public And<YearMonthSupplierBuilder, YearMonth> between(YearMonth startInclusive) {
        Objects.requireNonNull(startInclusive, "startInclusive");
        return endExclusive -> {
            if (!endExclusive.isAfter(startInclusive)) {
                throw new IllegalArgumentException("End YearMonth must be after start YearMonth.");
            }
            this.yearSupplier = IntegerSupplierBuilder.integerSupplier()
                    .between(startInclusive.getYear())
                    .and(endExclusive.getYear())
                    .build();
            this.monthSupplier = IntegerSupplierBuilder.integerSupplier()
                    .between(startInclusive.getMonthValue())
                    .and(endExclusive.getMonthValue())
                    .build();
            return this;
        };
    }
}
