package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.distribution.Distribution;
import net.jcip.annotations.ThreadSafe;
import org.djenni.distribution.Distribution;
import org.djenni.SerializableGenerator;
import org.hamcrest.Description;
import org.hamcrest.StringDescription;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

/**
 * A generator of random integers within the bounds defined by its configuration.
 */
@ThreadSafe
public class RandomIntegerGenerator implements SerializableGenerator<Integer> {
    private final int minInclusiveValue;
    private final int range;
    private final Distribution<Integer, Integer> distribution;

    /**
     * Construct the generator to create integer values in the desired manner.
     * @param minInclusiveValue The minimum value that can be generated - included in the range of values that can be
     *                          generated.
     * @param maxExclusiveValue The value just above the maximum value that can be generated - it is excluded from the
     *                          range of values that can be generated.
     * @param distribution      The desired distribution of values generated in the defined range.
     * @throws AssertionError   If the minimum and maximum values provide a range of less than 1
     */
    public RandomIntegerGenerator(int minInclusiveValue, int maxExclusiveValue, Distribution<Integer, Integer> distribution) {
        this.minInclusiveValue = minInclusiveValue;
        this.range = maxExclusiveValue - minInclusiveValue;
        assertThat("Range should be greater than 1", range, is(greaterThan(0)));
        this.distribution = distribution;
    }

    public Integer generate() {
        return minInclusiveValue + distribution.generate(range);
    }

    /**
     * Get the Distribution used in generating the integers.
     * @return the Distribution used by this generator.
     */
    public Distribution<Integer, Integer> getDistribution() {
        return distribution;
    }

    /**
     * Get the minimum inclusive value that can be generated.
     * @return the minimum inclusive value.
     */
    public int getMinInclusiveValue() {
        return minInclusiveValue;
    }

    /**
     * Get the range of values that can be generated. The difference between the minimum inclusive value and the maximum
     * exclusive value.
     * @return the range of values that can be generated.
     */
    public int getRange() {
        return range;
    }

    /**
     * Get the maximum exclusive value that can be generated.
     * @return the maximum exlusive value.
     */
    public int getMaxExclusiveValue() {
        return minInclusiveValue + range;
    }

    public void describeTo(Description description) {
        description
                .appendText("{ RandomIntegerGenerator : { minInclusiveValue : ")
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

        RandomIntegerGenerator that = (RandomIntegerGenerator) o;

        if (minInclusiveValue != that.minInclusiveValue) return false;
        if (range != that.range) return false;
        if (!distribution.equals(that.distribution)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = minInclusiveValue;
        result = 31 * result + range;
        result = 31 * result + distribution.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringDescription description = new StringDescription();
        description.appendDescriptionOf(this);
        return description.toString();
    }
}
