package com.robertboothby.djenni.time;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.lang.IntegerSupplierBuilder;
import com.robertboothby.djenni.sugar.And;

import java.time.Year;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Builds suppliers of {@link Year}.
 */
public class YearSupplierBuilder implements ConfigurableSupplierBuilder<Year, YearSupplierBuilder> {

    private Supplier<Integer> yearSupplier = IntegerSupplierBuilder.integerSupplier().build();

    public static YearSupplierBuilder aYear() {
        return new YearSupplierBuilder();
    }

    @Override
    public StreamableSupplier<Year> build() {
        Supplier<Integer> actualSupplier = this.yearSupplier;
        return () -> Year.of(actualSupplier.get());
    }

    public YearSupplierBuilder yearSupplier(Supplier<Integer> supplier) {
        this.yearSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public And<YearSupplierBuilder, Year> between(Year startInclusive) {
        Objects.requireNonNull(startInclusive, "startInclusive");
        return endExclusive -> {
            if (!endExclusive.isAfter(startInclusive)) {
                throw new IllegalArgumentException("End year must be after start year.");
            }
            this.yearSupplier = IntegerSupplierBuilder.integerSupplier()
                    .between(startInclusive.getValue())
                    .and(endExclusive.getValue())
                    .build();
            return this;
        };
    }
}
