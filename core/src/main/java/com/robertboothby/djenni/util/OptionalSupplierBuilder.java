package com.robertboothby.djenni.util;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.core.SupplierHelper;
import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.distribution.simple.SimpleRandomDoubleDistribution;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Builds suppliers of {@link Optional} values by wrapping an underlying {@link StreamableSupplier}. By default the
 * wrapped supplier is the framework {@link com.robertboothby.djenni.core.SupplierHelper#nullSupplier()} and values are
 * present 50% of the time using a uniform distribution.
 * @param <T> the wrapped type
 */
public class OptionalSupplierBuilder<T> implements ConfigurableSupplierBuilder<Optional<T>, OptionalSupplierBuilder<T>> {

    private StreamableSupplier<? extends T> valueSupplier = SupplierHelper.nullSupplier();
    private Distribution<Double, Double> presenceDistribution = SimpleRandomDoubleDistribution.UNIFORM;
    private double presenceProbability = 0.5D;

    public static <T> OptionalSupplierBuilder<T> optionalSupplier() {
        return new OptionalSupplierBuilder<>();
    }

    @Override
    public StreamableSupplier<Optional<T>> build() {
        StreamableSupplier<? extends T> actualValueSupplier = this.valueSupplier;
        Distribution<Double, Double> distribution = this.presenceDistribution;
        double probability = this.presenceProbability;
        return () -> distribution.generate(1.0D) < probability
                ? Optional.ofNullable(actualValueSupplier.get())
                : Optional.empty();
    }

    /**
     * Configure the supplier that should be wrapped inside the Optional.
     * @param supplier the supplier that provides the Optional value when present
     * @return this builder for further customisation
     */
    public OptionalSupplierBuilder<T> valueSupplier(Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier");
        this.valueSupplier = SupplierHelper.asStreamable(supplier);
        return this;
    }

    /**
     * Configure the supplier via another {@link SupplierBuilder}.
     * @param builder the builder providing the wrapped supplier
     * @return this builder for further customisation
     */
    public OptionalSupplierBuilder<T> valueSupplier(SupplierBuilder<? extends T> builder) {
        Objects.requireNonNull(builder, "builder");
        return valueSupplier(builder.build());
    }

    /**
     * Configure the likelihood that the Optional will contain a value.
     * @param probability probability between 0.0 and 1.0 inclusive
     * @return this builder for further customisation
     */
    public OptionalSupplierBuilder<T> presentWithProbability(double probability) {
        if (probability < 0.0D || probability > 1.0D) {
            throw new IllegalArgumentException("Probability must be between 0.0 and 1.0 inclusive.");
        }
        this.presenceProbability = probability;
        return this;
    }

    /**
     * Override the distribution used for determining whether the Optional is populated.
     * @param distribution the distribution to use
     * @return this builder for further customisation
     */
    public OptionalSupplierBuilder<T> withPresenceDistribution(Distribution<Double, Double> distribution) {
        this.presenceDistribution = Objects.requireNonNull(distribution, "distribution");
        return this;
    }
}
