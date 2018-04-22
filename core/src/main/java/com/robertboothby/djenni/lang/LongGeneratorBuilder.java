package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.distribution.simple.SimpleRandomLongDistribution;
import com.robertboothby.djenni.sugar.And;
import com.robertboothby.djenni.SerializableGenerator;
import com.robertboothby.djenni.SerializableGeneratorBuilder;
import com.robertboothby.djenni.core.GeneratorHelper;

/**
 * Builder designed to make it easy and expressive to configure a generator of Long values. Limited to a range
 * defined by the maximum value of a Long.
 * @author robertboothby
 */
public class LongGeneratorBuilder implements SerializableGeneratorBuilder<Long> {

    public static final long MIN_INCLUSIVE_VALUE_DEFAULT = 0;
    public static final long MAX_EXCLUSIVE_VALUE_DEFAULT = Long.MAX_VALUE;
    public static final Distribution<Long, Long> DISTRIBUTION_DEFAULT = SimpleRandomLongDistribution.UNIFORM;

    private long minInclusiveValue = MIN_INCLUSIVE_VALUE_DEFAULT;
    private long maxExclusiveValue = MAX_EXCLUSIVE_VALUE_DEFAULT;
    private Distribution<Long, Long> distribution = DISTRIBUTION_DEFAULT;

    public SerializableGenerator<Long> build() {
        if(maxExclusiveValue - minInclusiveValue == 1){
            return GeneratorHelper.$(minInclusiveValue);
        } else {
            return new RandomLongGenerator(minInclusiveValue, maxExclusiveValue, distribution);
        }
    }


    /**
     * Set the minimum and maximum inclusive values.
     * @param minInclusiveValue The minimum inclusive value.
     * @return The And on which to set the maximum exclusive value.
     */
    public And<LongGeneratorBuilder, Long> between(Long minInclusiveValue) {
        this.minInclusiveValue = minInclusiveValue;
        return new And<LongGeneratorBuilder, Long>() {
            public LongGeneratorBuilder and(Long maxExclusiveValue) {
                LongGeneratorBuilder.this.maxExclusiveValue = maxExclusiveValue;
                return LongGeneratorBuilder.this;
            }
        };
    }

    /**
     * Configure a non-even distribution of values.
     * @param distribution the distribution of values to use.
     * @return this builder for further configuration.
     */
    public LongGeneratorBuilder distribution(Distribution<Long, Long> distribution) {
        this.distribution = distribution;
        return this;
    }

    /**
     * Configure the integer generator to return a single, exact value;
     * @param onlyValue The only value that the integer generator will return.
     * @return this builder for further configuration.
     */
    public LongGeneratorBuilder onlyValue(Long onlyValue){
        this.minInclusiveValue = onlyValue;
        this.maxExclusiveValue = minInclusiveValue + 1;
        return this;
    }
    /**
     * Create an instance of the IntegerGeneratorBuilder for configuration - preconfigured to produce uniformly distributed
     * values between 0 and {@link Integer#MAX_VALUE}
     * @return the builder for configuration.
     */
    public static LongGeneratorBuilder generateALong() {
        return new LongGeneratorBuilder();
    }
}
