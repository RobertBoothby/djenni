package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.distribution.simple.SimpleRandomIntegerDistribution;
import com.robertboothby.djenni.sugar.And;
import net.jcip.annotations.NotThreadSafe;
import com.robertboothby.djenni.SerializableGenerator;
import com.robertboothby.djenni.SerializableGeneratorBuilder;

import static com.robertboothby.djenni.core.GeneratorHelper.$;

/**
 * Builder designed to make it easy and expressive to configure a generator of Integer values.
 * @author robertboothby
 */
@NotThreadSafe
public class IntegerGeneratorBuilder implements SerializableGeneratorBuilder<Integer> {

    public static final int MIN_INCLUSIVE_VALUE_DEFAULT = 0;
    public static final int MAX_EXCLUSIVE_VALUE_DEFAULT = Integer.MAX_VALUE;
    public static final Distribution<Integer, Integer> DISTRIBUTION_DEFAULT = SimpleRandomIntegerDistribution.UNIFORM;

    private int minInclusiveValue = MIN_INCLUSIVE_VALUE_DEFAULT;
    private int maxExclusiveValue = MAX_EXCLUSIVE_VALUE_DEFAULT;
    private Distribution<Integer, Integer> distribution = DISTRIBUTION_DEFAULT;

    public SerializableGenerator<Integer> build() {
        if(maxExclusiveValue - minInclusiveValue == 1){
            return $(minInclusiveValue);
        } else {
            return new RandomIntegerGenerator(minInclusiveValue, maxExclusiveValue, distribution);
        }
    }

    /**
     * Set the minimum and maximum inclusive values.
     * @param minInclusiveValue The minimum inclusive value.
     * @return The And on which to set the maximum exclusive value.
     */
    public And<IntegerGeneratorBuilder, Integer> between(int minInclusiveValue) {
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
    public IntegerGeneratorBuilder withDistribution(Distribution<Integer, Integer> distribution) {
        this.distribution = distribution;
        return this;
    }

    /**
     * Configure the integer generator to return a single, exact value;
     * @param onlyValue The only value that the integer generator will return.
     * @return this builder for further configuration.
     */
    public IntegerGeneratorBuilder onlyValue(int onlyValue){
        this.minInclusiveValue = onlyValue;
        this.maxExclusiveValue = minInclusiveValue + 1;
        return this;
    }

    /**
     * Create an instance of the IntegerGeneratorBuilder for configuration - preconfigured to produce uniformly distributed
     * values between 0 and {@link Integer#MAX_VALUE}
     * @return the builder for configuration.
     */
    public static IntegerGeneratorBuilder integerGenerator() {
        return new IntegerGeneratorBuilder();
    }
}
