package com.robertboothby.djenni.distribution.simple;

import com.robertboothby.djenni.distribution.Distribution;

/**
 * Integer-specialised façade for the {@link SimpleRandomDoubleDistribution} family. Each predefined constant wraps a
 * double-based distribution and scales the generated value into the requested integer bound using {@code floor}. The
 * bound is exclusive, so passing {@code 10} yields values {@code 0..9}. These helpers are intentionally simple and are
 * primarily meant for exercising edge-biased paths in tests rather than cryptographic scenarios.
 */
public class SimpleRandomIntegerDistribution implements Distribution<Integer, Integer> {

    private final SimpleRandomDoubleDistribution underlyingDistribution;

    /**
     * This distribution generates Integers following the distribution defined in {@link SimpleRandomLongDistribution#UNIFORM}.
     */
    public static final SimpleRandomIntegerDistribution UNIFORM =
            new SimpleRandomIntegerDistribution(SimpleRandomDoubleDistribution.UNIFORM);

    /**
     * This distribution generates Integers following the distribution defined in {@link SimpleRandomLongDistribution#NORMAL}.
     */
    public static final SimpleRandomIntegerDistribution NORMAL =
            new SimpleRandomIntegerDistribution(SimpleRandomDoubleDistribution.NORMAL);

    /**
     * This distribution generates Integers following the distribution defined in {@link SimpleRandomLongDistribution#LEFT_NORMAL}.
     */
    public static final SimpleRandomIntegerDistribution LEFT_NORMAL =
            new SimpleRandomIntegerDistribution(SimpleRandomDoubleDistribution.LEFT_NORMAL);

    /**
     * This distribution generates Integers following the distribution defined in {@link SimpleRandomLongDistribution#RIGHT_NORMAL}.
     */
    public static final SimpleRandomIntegerDistribution RIGHT_NORMAL =
            new SimpleRandomIntegerDistribution(SimpleRandomDoubleDistribution.RIGHT_NORMAL);

    /**
     * This distribution generates Integers following the distribution defined in {@link SimpleRandomLongDistribution#INVERTED_NORMAL}.
     */
    public static final SimpleRandomIntegerDistribution INVERTED_NORMAL =
            new SimpleRandomIntegerDistribution(SimpleRandomDoubleDistribution.INVERTED_NORMAL);

    /**
     * This distribution generates Integers following the distribution defined in {@link SimpleRandomLongDistribution#LEFT_INVERTED_NORMAL}.
     */
    public static final SimpleRandomIntegerDistribution LEFT_INVERTED_NORMAL =
            new SimpleRandomIntegerDistribution(SimpleRandomDoubleDistribution.LEFT_INVERTED_NORMAL);

    /**
     * This distribution generates Integers following the distribution defined in {@link SimpleRandomLongDistribution#RIGHT_INVERTED_NORMAL}.
     */
    public static final SimpleRandomIntegerDistribution RIGHT_INVERTED_NORMAL =
            new SimpleRandomIntegerDistribution(SimpleRandomDoubleDistribution.RIGHT_INVERTED_NORMAL);

    /**
     * Build a new integer distribution backed by the supplied double distribution. Capturing the delegate allows custom
     * probability curves to be injected where needed.
     */
    public SimpleRandomIntegerDistribution(SimpleRandomDoubleDistribution underlyingDistribution) {
        this.underlyingDistribution = underlyingDistribution;
    }

    /**
     * Generate the next integer in {@code [0, bound)}. A {@link IllegalArgumentException} will be thrown if the bound
     * is {@code null} or not positive.
     */
    public Integer generate(Integer bound) {
        if (bound == null || bound <= 0) {
            throw new IllegalArgumentException("Bound must be positive");
        }
        return (int) Math.floor(underlyingDistribution.nextDouble() * bound);
    }
}
