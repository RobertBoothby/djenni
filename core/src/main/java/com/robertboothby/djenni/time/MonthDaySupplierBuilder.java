package com.robertboothby.djenni.time;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.lang.IntegerSupplierBuilder;
import com.robertboothby.djenni.sugar.And;

import java.time.MonthDay;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Builds suppliers of {@link MonthDay}.
 */
public class MonthDaySupplierBuilder implements ConfigurableSupplierBuilder<MonthDay, MonthDaySupplierBuilder> {

    private Supplier<Integer> monthSupplier = IntegerSupplierBuilder.integerSupplier().between(1).and(13).build();
    private Supplier<Integer> daySupplier = IntegerSupplierBuilder.integerSupplier().between(1).and(32).build();

    public static MonthDaySupplierBuilder aMonthDay() {
        return new MonthDaySupplierBuilder();
    }

    @Override
    public StreamableSupplier<MonthDay> build() {
        Supplier<Integer> months = this.monthSupplier;
        Supplier<Integer> days = this.daySupplier;
        return () -> MonthDay.of(months.get(), days.get());
    }

    public MonthDaySupplierBuilder monthSupplier(Supplier<Integer> supplier) {
        this.monthSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public MonthDaySupplierBuilder daySupplier(Supplier<Integer> supplier) {
        this.daySupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public And<MonthDaySupplierBuilder, MonthDay> between(MonthDay startInclusive) {
        Objects.requireNonNull(startInclusive, "startInclusive");
        return endExclusive -> {
            if (!endExclusive.isAfter(startInclusive)) {
                throw new IllegalArgumentException("End MonthDay must be after start MonthDay.");
            }
            this.monthSupplier = IntegerSupplierBuilder.integerSupplier()
                    .between(startInclusive.getMonthValue())
                    .and(endExclusive.getMonthValue())
                    .build();
            this.daySupplier = IntegerSupplierBuilder.integerSupplier()
                    .between(startInclusive.getDayOfMonth())
                    .and(endExclusive.getDayOfMonth())
                    .build();
            return this;
        };
    }
}
