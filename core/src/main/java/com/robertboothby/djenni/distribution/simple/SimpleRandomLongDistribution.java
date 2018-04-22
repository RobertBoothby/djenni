package com.robertboothby.djenni.distribution.simple;

import com.robertboothby.djenni.distribution.Distribution;
import org.hamcrest.Description;

/**
 *
 * @author robertboothby
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

    public SimpleRandomLongDistribution(SimpleRandomDoubleDistribution underlyingDistribution) {
        this.underlyingDistribution = underlyingDistribution;
    }

    public Long generate(Long bound) {
        return (long) Math.floor(underlyingDistribution.nextDouble() * bound);
    }

    public void describeTo(Description description) {
        description
                .appendText("LongDoubleBasedDistribution { ")
                .appendDescriptionOf(underlyingDistribution)
                .appendText(" } ");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleRandomLongDistribution that = (SimpleRandomLongDistribution) o;

        if (!underlyingDistribution.equals(that.underlyingDistribution)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return underlyingDistribution.hashCode();
    }
}
