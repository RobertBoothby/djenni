package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.core.ExplicitlyBiassedSupplier;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.distribution.simple.SimpleRandomDoubleDistribution;

/**
 * Trivial implementation of the {@link SupplierBuilder} inteface that is created to retain consistency with the other
 * primitive types. When explicit control is required then it is better to consider using the
 * {@link ExplicitlyBiassedSupplier}.
 *
 * @author robertboothby
 */
public class BooleanSupplierBuilder implements ConfigurableSupplierBuilder<Boolean, BooleanSupplierBuilder> {

    private static final Distribution<Double, Double> DISTRIBUTION_DEFAULT = SimpleRandomDoubleDistribution.UNIFORM;
    private Distribution<Double, Double> distribution = DISTRIBUTION_DEFAULT;

    public static BooleanSupplierBuilder booleanSupplier() {
        return new BooleanSupplierBuilder();
    }

    public StreamableSupplier<Boolean> build() {
        Distribution<Double, Double> actualDistribution = distribution;
        return () -> actualDistribution.generate(1.0D) < 0.5D;
    }

    /**
     * Configure the distribution to be used with the Suppliers that this will build.
     * @param distribution the distribution to use in any Suppliers built.
     * @return the Supplier builder for further configuration.
     */
    public BooleanSupplierBuilder withDistribution(Distribution<Double, Double> distribution) {
        this.distribution = distribution;
        return this;
    }
}
