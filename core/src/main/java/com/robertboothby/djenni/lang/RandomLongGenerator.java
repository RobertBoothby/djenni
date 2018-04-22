package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.SerializableGenerator;
import com.robertboothby.djenni.distribution.Distribution;
import org.hamcrest.Description;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

/**
 * A generator of random longs within the bounds defined by its configuration.
 * @author robertboothby
 */
public class RandomLongGenerator implements SerializableGenerator<Long> {

    private final long minInclusiveValue;
    private final long range;
    private final Distribution<Long, Long> distribution;

    /**
     * Construct the generator to create integer values in the desired manner.
     * @param minInclusiveValue The minimum value that can be generated - included in the range of values that can be
     *                          generated.
     * @param maxExclusiveValue The value just above the maximum value that can be generated - it is excluded from the
     *                          range of values that can be generated.
     * @param distribution      The desired distribution of values generated in the defined range.
     * @throws AssertionError   If the minimum and maximum values provide a range of less than 1 either because the
     *                          minimum is greater than the maximum or because the range covered is greater than can be
     *                          represented by a single Long.
     */
    public RandomLongGenerator(long minInclusiveValue, long maxExclusiveValue, Distribution<Long, Long> distribution) {
        this.minInclusiveValue = minInclusiveValue;
        this.range = maxExclusiveValue - minInclusiveValue;
        assertThat("Range should be greater than 0 but less than " + Long.MAX_VALUE, range, is(greaterThan(0L)));
        this.distribution = distribution;
    }

    public Long generate() {
        return minInclusiveValue + distribution.generate(range);
    }

    public void describeTo(Description description) {
        description
                .appendText("{ RandomLongGenerator : { minInclusiveValue : ")
                .appendValue(minInclusiveValue)
                .appendText(", range : ")
                .appendValue(range)
                .appendText(", distribution : ")
                .appendDescriptionOf(distribution)
                .appendText(" } }");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RandomLongGenerator that = (RandomLongGenerator) o;

        if (minInclusiveValue != that.minInclusiveValue) return false;
        if (range != that.range) return false;
        if (!distribution.equals(that.distribution)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (minInclusiveValue ^ (minInclusiveValue >>> 32));
        result = 31 * result + (int) (range ^ (range >>> 32));
        result = 31 * result + distribution.hashCode();
        return result;
    }
}
