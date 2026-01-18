package com.robertboothby.djenni.distribution.simple;

import com.robertboothby.djenni.distribution.Distribution;

/**
 * Equivalent to {@link SimpleRandomIntegerDistribution} but for {@link Long} bounds/values. Long distributions reuse the
 * double-based implementations, giving us normal/left/right/inverted curves without duplicating the maths. Their main
 * usage is in builders that need positive long ranges (for example epoch milliseconds).
 */
public class SimpleRandomLongDistribution implements Distribution<Long, Long> {

    private final SimpleRandomDoubleDistribution underlyingDistribution;

    /**
     * This distribution generates Longs following the distribution defined in {@link SimpleRandomLongDistribution#UNIFORM}.
     */
    public static final SimpleRandomLongDistribution UNIFORM =
            new SimpleRandomLongDistribution(SimpleRandomDoubleDistribution.UNIFORM);

    /**
     * This distribution generates Longs following the distribution defined in {@link SimpleRandomLongDistribution#NORMAL}.
     */
    public static final SimpleRandomLongDistribution NORMAL =
            new SimpleRandomLongDistribution(SimpleRandomDoubleDistribution.NORMAL);

    /**
     * This distribution generates Longs following the distribution defined in {@link SimpleRandomLongDistribution#LEFT_NORMAL}.
     */
    public static final SimpleRandomLongDistribution LEFT_NORMAL =
            new SimpleRandomLongDistribution(SimpleRandomDoubleDistribution.LEFT_NORMAL);

    /**
     * This distribution generates Longs following the distribution defined in {@link SimpleRandomLongDistribution#RIGHT_NORMAL}.
     */
    public static final SimpleRandomLongDistribution RIGHT_NORMAL =
            new SimpleRandomLongDistribution(SimpleRandomDoubleDistribution.RIGHT_NORMAL);

    /**
     * This distribution generates Longs following the distribution defined in {@link SimpleRandomLongDistribution#INVERTED_NORMAL}.
     */
    public static final SimpleRandomLongDistribution INVERTED_NORMAL =
            new SimpleRandomLongDistribution(SimpleRandomDoubleDistribution.INVERTED_NORMAL);

    /**
     * This distribution generates Longs following the distribution defined in {@link SimpleRandomLongDistribution#LEFT_INVERTED_NORMAL}.
     */
    public static final SimpleRandomLongDistribution LEFT_INVERTED_NORMAL =
            new SimpleRandomLongDistribution(SimpleRandomDoubleDistribution.LEFT_INVERTED_NORMAL);

    /**
     * This distribution generates Longs following the distribution defined in {@link SimpleRandomLongDistribution#RIGHT_INVERTED_NORMAL}.
     */
    public static final SimpleRandomLongDistribution RIGHT_INVERTED_NORMAL =
            new SimpleRandomLongDistribution(SimpleRandomDoubleDistribution.RIGHT_INVERTED_NORMAL);

    /**
     * Construct an instance of the distribution using the underlying distribution.
     * @param underlyingDistribution The underlying distribution to use.
     */
    /**
     * Build a new long distribution backed by the supplied double distribution. The delegate controls the shape of the
     * probability curve.
     */
    public SimpleRandomLongDistribution(SimpleRandomDoubleDistribution underlyingDistribution) {
        this.underlyingDistribution = underlyingDistribution;
    }

    @Override
    public Long generate(Long bound) {
        if (bound == null || bound <= 0L) {
            throw new IllegalArgumentException("Bound must be positive");
        }
        return (long) Math.floor(underlyingDistribution.nextDouble() * bound);
    }

}
