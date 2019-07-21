package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.core.SupplierHelper;
import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.distribution.simple.SimpleRandomLongDistribution;
import com.robertboothby.djenni.sugar.And;

import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

/**
 * Builder designed to make it easy and expressive to configure a generator of Long values. Limited to a range
 * defined by the maximum value of a Long.
 * @author robertboothby
 */
public class LongSupplierBuilder implements ConfigurableSupplierBuilder<Long, LongSupplierBuilder> {

    public static final long MIN_INCLUSIVE_VALUE_DEFAULT = 0;
    public static final long MAX_EXCLUSIVE_VALUE_DEFAULT = Long.MAX_VALUE;
    public static final Distribution<Long, Long> DISTRIBUTION_DEFAULT = SimpleRandomLongDistribution.UNIFORM;

    private long minInclusiveValue = MIN_INCLUSIVE_VALUE_DEFAULT;
    private long maxExclusiveValue = MAX_EXCLUSIVE_VALUE_DEFAULT;
    private Distribution<Long, Long> distribution = DISTRIBUTION_DEFAULT;

    public StreamableSupplier<Long> build() {
        long minInclusiveValue = this.minInclusiveValue;
        long maxExclusiveValue = this.maxExclusiveValue;
        Distribution<Long, Long> distribution = this.distribution;
        if(maxExclusiveValue - minInclusiveValue == 1){
            return SupplierHelper.fix(minInclusiveValue);
        } else {
            Long range = maxExclusiveValue - minInclusiveValue;
            assertThat("Range should be greater than 0 but less than " + Long.MAX_VALUE, range, is(greaterThan(0L)));
            return () -> minInclusiveValue + distribution.generate(range);
        }
    }


    /**
     * Set the minimum and maximum inclusive values.
     * @param minInclusiveValue The minimum inclusive value.
     * @return The And on which to set the maximum exclusive value.
     */
    public And<LongSupplierBuilder, Long> between(Long minInclusiveValue) {
        this.minInclusiveValue = minInclusiveValue;
        return new And<LongSupplierBuilder, Long>() {
            public LongSupplierBuilder and(Long maxExclusiveValue) {
                LongSupplierBuilder.this.maxExclusiveValue = maxExclusiveValue;
                return LongSupplierBuilder.this;
            }
        };
    }

    /**
     * Configure a non-even distribution of values.
     * @param distribution the distribution of values to use.
     * @return this builder for further configuration.
     */
    public LongSupplierBuilder distribution(Distribution<Long, Long> distribution) {
        this.distribution = distribution;
        return this;
    }

    /**
     * Configure the integer generator to return a single, exact value;
     * @param onlyValue The only value that the integer generator will return.
     * @return this builder for further configuration.
     */
    public LongSupplierBuilder onlyValue(Long onlyValue){
        this.minInclusiveValue = onlyValue;
        this.maxExclusiveValue = minInclusiveValue + 1;
        return this;
    }
    /**
     * Create an instance of the IntegerSupplierBuilder for configuration - preconfigured to produce uniformly distributed
     * values between 0 and {@link Integer#MAX_VALUE}
     * @return the builder for configuration.
     */
    public static LongSupplierBuilder generateALong() {
        return new LongSupplierBuilder();
    }
}
