package com.robertboothby.djenni.distribution.simple;

import com.robertboothby.djenni.distribution.Distribution;
import org.hamcrest.Description;

/**
 * This class holds the current set of simple integer distributions. All the distributions are based on those found in
 * {@link com.robertboothby.djenni.distribution.simple.SimpleRandomBigIntegerDistribution}.
 * @author robertboothby
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

    public SimpleRandomIntegerDistribution(SimpleRandomDoubleDistribution underlyingDistribution) {
        this.underlyingDistribution = underlyingDistribution;
    }

    public Integer generate(Integer bound) {
        return (int) Math.floor(underlyingDistribution.nextDouble() * bound);
    }

    public void describeTo(Description description) {
        description
                .appendText("asIntegerDistribution { ")
                .appendDescriptionOf(underlyingDistribution)
                .appendText(" } ");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleRandomIntegerDistribution that = (SimpleRandomIntegerDistribution) o;

        if (!underlyingDistribution.equals(that.underlyingDistribution)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return underlyingDistribution.hashCode();
    }
}
