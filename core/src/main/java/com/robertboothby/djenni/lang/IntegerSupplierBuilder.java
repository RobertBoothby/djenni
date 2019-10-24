package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.distribution.simple.SimpleRandomIntegerDistribution;
import com.robertboothby.djenni.sugar.And;

import java.util.function.Consumer;

import static com.robertboothby.djenni.core.SupplierHelper.fix;

/**
 * Builder designed to make it easy and expressive to configure a supplier of Integer values.
 * TODO consider the range of values.
 * @author robertboothby
 */
public class IntegerSupplierBuilder implements ConfigurableSupplierBuilder<Integer, IntegerSupplierBuilder> {

    public static final int MIN_INCLUSIVE_VALUE_DEFAULT = 0;
    public static final int MAX_EXCLUSIVE_VALUE_DEFAULT = Integer.MAX_VALUE;
    public static final Distribution<Integer, Integer> DISTRIBUTION_DEFAULT = SimpleRandomIntegerDistribution.UNIFORM;

    private int minInclusiveValue = MIN_INCLUSIVE_VALUE_DEFAULT;
    private int maxExclusiveValue = MAX_EXCLUSIVE_VALUE_DEFAULT;
    private Distribution<Integer, Integer> distribution = DISTRIBUTION_DEFAULT;
    private boolean sequential = false;

    public StreamableSupplier<Integer> build() {
        int minInclusiveValue = this.minInclusiveValue;
        int maxExclusiveValue = this.maxExclusiveValue;
        Distribution<Integer, Integer> distribution = this.distribution;
        boolean sequential = this.sequential;

        if(maxExclusiveValue - minInclusiveValue == 1){
            return fix(minInclusiveValue);
        } else if (sequential){
            ThreadLocal<Integer> perThread = ThreadLocal.withInitial(() -> minInclusiveValue);
            return () -> {
                int value = perThread.get();
                int newValue = value + 1;
                if(newValue >= maxExclusiveValue){
                    newValue = minInclusiveValue;
                }
                perThread.set(newValue);
                return value;
            };
        } else {
            int range = maxExclusiveValue - minInclusiveValue;
            return () -> minInclusiveValue + distribution.generate(range);
        }
    }

    /**
     * Set the minimum and maximum inclusive values.
     * @param minInclusiveValue The minimum inclusive value.
     * @return The And on which to set the maximum exclusive value.
     */
    public And<IntegerSupplierBuilder, Integer> between(int minInclusiveValue) {
        this.minInclusiveValue = minInclusiveValue;
        return maxExclusiveValue -> {
            this.maxExclusiveValue = maxExclusiveValue;
            return this;
        };
    }

    /**
     * Configure a non-even distribution of values.
     * @param distribution the distribution of values to use.
     * @return this builder for further configuration.
     */
    public IntegerSupplierBuilder withDistribution(Distribution<Integer, Integer> distribution) {
        this.distribution = distribution;
        return this;
    }

    /**
     * Configure the supplier being built to supply the range sequentially within the range of values rolling over to
     * the beginning when the range is exhausted.
     * @return this builder for further configuration.
     */
    public IntegerSupplierBuilder sequential() {
        this.sequential = true;
        return this;
    }

    /**
     * Configure the supplier being built to be random within the range of values. This is the default configuration.
     * @return this builder for further configuration.
     */
    public IntegerSupplierBuilder random() {
        this.sequential = false;
        return this;
    }


    /**
     * Configure the integer generator to return a single, exact value;
     * @param onlyValue The only value that the integer generator will return.
     * @return this builder for further configuration.
     */
    public IntegerSupplierBuilder onlyValue(int onlyValue){
        this.minInclusiveValue = onlyValue;
        this.maxExclusiveValue = minInclusiveValue + 1;
        return this;
    }

    /**
     * Create an instance of the IntegerSupplierBuilder for configuration.
     * @return the builder for configuration.
     */
    public static IntegerSupplierBuilder integerSupplier() {
        return new IntegerSupplierBuilder();
    }

    /**
     * Create a Supplier of Integers using the given configuration.
     * @param configuration The configuration to use.
     * @return The configured Supplier, ready to use.
     */
    public static StreamableSupplier<Integer> integerSupplier(Consumer<IntegerSupplierBuilder> configuration){
        IntegerSupplierBuilder builder = new IntegerSupplierBuilder();
        configuration.accept(builder);
        return builder.build();
    }
}
