package com.robertboothby.djenni.util;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.core.SupplierHelper;
import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.distribution.simple.SimpleRandomDoubleDistribution;

import java.util.Objects;
import java.util.OptionalDouble;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

/**
 * Builds suppliers of {@link OptionalDouble} values by wrapping another supplier. The default configuration generates
 * the value {@code 0.0D} when present and uses a uniform 50% chance of presence.
 */
public class OptionalDoubleSupplierBuilder implements ConfigurableSupplierBuilder<OptionalDouble, OptionalDoubleSupplierBuilder> {

    private DoubleSupplier valueSupplier = () -> 0.0D;
    private Distribution<Double, Double> presenceDistribution = SimpleRandomDoubleDistribution.UNIFORM;
    private double presenceProbability = 0.5D;

    public static OptionalDoubleSupplierBuilder optionalDoubleSupplier() {
        return new OptionalDoubleSupplierBuilder();
    }

    @Override
    public StreamableSupplier<OptionalDouble> build() {
        DoubleSupplier actualSupplier = this.valueSupplier;
        Distribution<Double, Double> distribution = this.presenceDistribution;
        double probability = this.presenceProbability;
        return () -> distribution.generate(1.0D) < probability
                ? OptionalDouble.of(actualSupplier.getAsDouble())
                : OptionalDouble.empty();
    }

    public OptionalDoubleSupplierBuilder valueSupplier(DoubleSupplier supplier) {
        this.valueSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public OptionalDoubleSupplierBuilder valueSupplier(Supplier<? extends Double> supplier) {
        Objects.requireNonNull(supplier, "supplier");
        StreamableSupplier<? extends Double> streamable = SupplierHelper.asStreamable(supplier);
        this.valueSupplier = () -> streamable.get();
        return this;
    }

    public OptionalDoubleSupplierBuilder valueSupplier(SupplierBuilder<? extends Double> builder) {
        Objects.requireNonNull(builder, "builder");
        return valueSupplier(builder.build());
    }

    public OptionalDoubleSupplierBuilder presentWithProbability(double probability) {
        if (probability < 0.0D || probability > 1.0D) {
            throw new IllegalArgumentException("Probability must be between 0.0 and 1.0 inclusive.");
        }
        this.presenceProbability = probability;
        return this;
    }

    public OptionalDoubleSupplierBuilder withPresenceDistribution(Distribution<Double, Double> distribution) {
        this.presenceDistribution = Objects.requireNonNull(distribution, "distribution");
        return this;
    }
}
