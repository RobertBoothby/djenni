package com.robertboothby.djenni.util;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.core.SupplierHelper;
import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.distribution.simple.SimpleRandomDoubleDistribution;

import java.util.Objects;
import java.util.OptionalLong;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

/**
 * Builds suppliers of {@link OptionalLong} values by wrapping another supplier. The default configuration generates the
 * value {@code 0L} when present and uses a uniform 50% chance of presence.
 */
public class OptionalLongSupplierBuilder implements ConfigurableSupplierBuilder<OptionalLong, OptionalLongSupplierBuilder> {

    private LongSupplier valueSupplier = () -> 0L;
    private Distribution<Double, Double> presenceDistribution = SimpleRandomDoubleDistribution.UNIFORM;
    private double presenceProbability = 0.5D;

    public static OptionalLongSupplierBuilder optionalLongSupplier() {
        return new OptionalLongSupplierBuilder();
    }

    @Override
    public StreamableSupplier<OptionalLong> build() {
        LongSupplier actualSupplier = this.valueSupplier;
        Distribution<Double, Double> distribution = this.presenceDistribution;
        double probability = this.presenceProbability;
        return () -> distribution.generate(1.0D) < probability
                ? OptionalLong.of(actualSupplier.getAsLong())
                : OptionalLong.empty();
    }

    public OptionalLongSupplierBuilder valueSupplier(LongSupplier supplier) {
        this.valueSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public OptionalLongSupplierBuilder valueSupplier(Supplier<? extends Long> supplier) {
        Objects.requireNonNull(supplier, "supplier");
        StreamableSupplier<? extends Long> streamable = SupplierHelper.asStreamable(supplier);
        this.valueSupplier = () -> streamable.get();
        return this;
    }

    public OptionalLongSupplierBuilder valueSupplier(SupplierBuilder<? extends Long> builder) {
        Objects.requireNonNull(builder, "builder");
        return valueSupplier(builder.build());
    }

    public OptionalLongSupplierBuilder presentWithProbability(double probability) {
        if (probability < 0.0D || probability > 1.0D) {
            throw new IllegalArgumentException("Probability must be between 0.0 and 1.0 inclusive.");
        }
        this.presenceProbability = probability;
        return this;
    }

    public OptionalLongSupplierBuilder withPresenceDistribution(Distribution<Double, Double> distribution) {
        this.presenceDistribution = Objects.requireNonNull(distribution, "distribution");
        return this;
    }
}
