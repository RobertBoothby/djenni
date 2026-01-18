package com.robertboothby.djenni.util;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.core.SupplierHelper;
import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.distribution.simple.SimpleRandomDoubleDistribution;

import java.util.Objects;
import java.util.OptionalInt;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * Builds suppliers of {@link OptionalInt} values by wrapping another supplier. The default configuration generates the
 * value {@code 0} when present and uses a uniform 50% chance of presence.
 */
public class OptionalIntSupplierBuilder implements ConfigurableSupplierBuilder<OptionalInt, OptionalIntSupplierBuilder> {

    private IntSupplier valueSupplier = () -> 0;
    private Distribution<Double, Double> presenceDistribution = SimpleRandomDoubleDistribution.UNIFORM;
    private double presenceProbability = 0.5D;

    public static OptionalIntSupplierBuilder optionalIntSupplier() {
        return new OptionalIntSupplierBuilder();
    }

    @Override
    public StreamableSupplier<OptionalInt> build() {
        IntSupplier actualSupplier = this.valueSupplier;
        Distribution<Double, Double> distribution = this.presenceDistribution;
        double probability = this.presenceProbability;
        return () -> distribution.generate(1.0D) < probability
                ? OptionalInt.of(actualSupplier.getAsInt())
                : OptionalInt.empty();
    }

    public OptionalIntSupplierBuilder valueSupplier(IntSupplier supplier) {
        this.valueSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public OptionalIntSupplierBuilder valueSupplier(Supplier<? extends Integer> supplier) {
        Objects.requireNonNull(supplier, "supplier");
        StreamableSupplier<? extends Integer> streamable = SupplierHelper.asStreamable(supplier);
        this.valueSupplier = () -> streamable.get();
        return this;
    }

    public OptionalIntSupplierBuilder valueSupplier(SupplierBuilder<? extends Integer> builder) {
        Objects.requireNonNull(builder, "builder");
        return valueSupplier(builder.build());
    }

    public OptionalIntSupplierBuilder presentWithProbability(double probability) {
        if (probability < 0.0D || probability > 1.0D) {
            throw new IllegalArgumentException("Probability must be between 0.0 and 1.0 inclusive.");
        }
        this.presenceProbability = probability;
        return this;
    }

    public OptionalIntSupplierBuilder withPresenceDistribution(Distribution<Double, Double> distribution) {
        this.presenceDistribution = Objects.requireNonNull(distribution, "distribution");
        return this;
    }
}
