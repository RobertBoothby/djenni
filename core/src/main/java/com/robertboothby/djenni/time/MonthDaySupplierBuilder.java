package com.robertboothby.djenni.time;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.lang.IntegerSupplierBuilder;
import com.robertboothby.djenni.sugar.And;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Builds suppliers of {@link MonthDay}.
 * <p>
 * The default configuration treats every day in the requested range uniformly and therefore includes
 * {@code 29 February} whenever the range spans it. Call {@link #preventLeapDay(boolean)} to exclude leap day entirely.
 * If you instead need to guarantee at least one leap-day emission, compose this supplier with other core components
 * (for example, bias {@link MonthDay#of(int, int) MonthDay.of(2, 29)} via {@code ExplicitlyBiassedSupplierBuilder} or
 * merge two {@link StreamableSupplier} instances with {@link com.robertboothby.djenni.core.SupplierHelper SupplierHelper}
 * helpers) to prioritise that value.
 */
public class MonthDaySupplierBuilder implements ConfigurableSupplierBuilder<MonthDay, MonthDaySupplierBuilder> {
    private static final MonthDay LEAP_DAY = MonthDay.of(2, 29);

    private Supplier<Integer> monthSupplier = IntegerSupplierBuilder.integerSupplier().between(1).and(13).build();
    private Supplier<Integer> daySupplier = IntegerSupplierBuilder.integerSupplier().between(1).and(32).build();
    private StreamableSupplier<MonthDay> rangeSupplier;
    private boolean preventLeapDay;

    public static MonthDaySupplierBuilder aMonthDay() {
        return new MonthDaySupplierBuilder();
    }

    @Override
    public StreamableSupplier<MonthDay> build() {
        StreamableSupplier<MonthDay> rangeSupplier = this.rangeSupplier;
        if (rangeSupplier != null) {
            return rangeSupplier;
        }
        Supplier<Integer> months = this.monthSupplier;
        Supplier<Integer> days = this.daySupplier;
        return () -> MonthDay.of(months.get(), days.get());
    }

    public MonthDaySupplierBuilder monthSupplier(Supplier<Integer> supplier) {
        this.rangeSupplier = null;
        this.monthSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public MonthDaySupplierBuilder daySupplier(Supplier<Integer> supplier) {
        this.rangeSupplier = null;
        this.daySupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    /**
     * Configure whether leap day values should be excluded when generating ranges via {@link #between(MonthDay)}.
     * This has no effect when supplying custom month/day suppliers directly.
     * @param preventLeapDay {@code true} to suppress {@code 29 February}, {@code false} to allow it when present.
     * @return this builder for further configuration.
     */
    public MonthDaySupplierBuilder preventLeapDay(boolean preventLeapDay) {
        this.rangeSupplier = null;
        this.preventLeapDay = preventLeapDay;
        return this;
    }

    public And<MonthDaySupplierBuilder, MonthDay> between(MonthDay startInclusive) {
        Objects.requireNonNull(startInclusive, "startInclusive");
        return endExclusive -> {
            if (!endExclusive.isAfter(startInclusive)) {
                throw new IllegalArgumentException("End MonthDay must be after start MonthDay.");
            }
            LocalDate baseStart = startInclusive.atYear(2000);
            LocalDate baseEnd = endExclusive.atYear(2000);
            long daySpan = ChronoUnit.DAYS.between(baseStart, baseEnd);
            assert daySpan <= 366 : "MonthDay range unexpectedly exceeds a calendar year.";
            List<MonthDay> values = Stream.iterate(baseStart, date -> date.plusDays(1))
                    .limit(daySpan)
                    .map(MonthDay::from)
                    .filter(value -> !(preventLeapDay && LEAP_DAY.equals(value)))
                    .collect(Collectors.toList());
            if (values.isEmpty()) {
                throw new IllegalArgumentException("Preventing leap day removed all values from the requested range.");
            }
            StreamableSupplier<Integer> indices = IntegerSupplierBuilder.integerSupplier()
                    .between(0)
                    .and(values.size())
                    .build();
            this.rangeSupplier = indices.derive(values::get);
            return this;
        };
    }
}
