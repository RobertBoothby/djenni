package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.distribution.simple.SimpleRandomDoubleDistribution;         
import com.robertboothby.djenni.sugar.And;

import java.util.function.Consumer;


/**
 * Simple implementation of a Double Supplier. It cannot supply the full range of values as it works on an exclusive basis
 * and can only gwnerate a range of values constrained by {@link Double#MAX_VALUE}.
 */
public class DoubleSupplierBuilder implements ConfigurableSupplierBuilder<Double, DoubleSupplierBuilder> {

    private Distribution<Double, Double> distribution = SimpleRandomDoubleDistribution.UNIFORM;

    private Double minimumValue = 0.0D;

    private Double range = 0.0D;

    @Override
    public StreamableSupplier<Double> build() {
        Double minimumValue = this.minimumValue;
        Double range = this.range;
        Distribution<Double, Double> distribution = this.distribution;
        return () -> minimumValue + distribution.generate(range);
    }

    /**
     * Define the base distribution to use. Default is {@link SimpleRandomDoubleDistribution#UNIFORM}.
     * @param distribution The distribution to use.
     * @return The builder for further configuration,
     */
    public DoubleSupplierBuilder withDistribution(Distribution<Double, Double> distribution) {
        this.distribution = distribution;
        return this;
    }

    /**
     * Define the range of values to be supplied between minimum value (inclusive) and maximum valuee exclusive.
     * @param minimumValue The minimum value to use.
     * @return The object on which to configure the maximum value.
     */
    public And<DoubleSupplierBuilder, Double> between(double minimumValue){
        this.minimumValue = minimumValue;
        return maximumValue -> {
            this.range  = maximumValue - minimumValue;
            if(range < 0D){
                throw new IllegalStateException("Invalid range of values defined.");
            }
            return this;
        };
    }

    /**
     * Get an instance of the DoubleSupplierBuilder created with reasonable defaults for further configuration.
     * @return an instance of DoubleSupplierBuilder.
     */
    public static DoubleSupplierBuilder doubles(){
        return new DoubleSupplierBuilder();
    }

    /**
     * Get an instance of the supplier based on the configuration and reasonable defaults.
     * @param configuration The configuration to use.
     * @return A configured StreamableSupplier of Doubles.
     */
    public static StreamableSupplier<Double> doubles(Consumer<DoubleSupplierBuilder> configuration){
        return doubles().build(configuration);
    }
}
