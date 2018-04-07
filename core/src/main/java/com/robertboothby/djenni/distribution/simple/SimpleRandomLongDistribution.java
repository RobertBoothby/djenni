package com.robertboothby.djenni.distribution.simple;

import org.djenni.distribution.Distribution;
import org.hamcrest.Description;

/**
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 *
 * @author robertboothby
 */
public class SimpleRandomLongDistribution implements Distribution<Long, Long> {

    private final SimpleRandomDoubleDistribution underlyingDistribution;

    public static final SimpleRandomLongDistribution UNIFORM =
            new SimpleRandomLongDistribution(SimpleRandomDoubleDistribution.UNIFORM);
    public static final SimpleRandomLongDistribution NORMAL =
            new SimpleRandomLongDistribution(SimpleRandomDoubleDistribution.NORMAL);
    public static final SimpleRandomLongDistribution LEFT_NORMAL =
            new SimpleRandomLongDistribution(SimpleRandomDoubleDistribution.LEFT_NORMAL);
    public static final SimpleRandomLongDistribution RIGHT_NORMAL =
            new SimpleRandomLongDistribution(SimpleRandomDoubleDistribution.RIGHT_NORMAL);
    public static final SimpleRandomLongDistribution INVERTED_NORMAL =
            new SimpleRandomLongDistribution(SimpleRandomDoubleDistribution.INVERTED_NORMAL);
    public static final SimpleRandomLongDistribution LEFT_INVERTED_NORMAL =
            new SimpleRandomLongDistribution(SimpleRandomDoubleDistribution.LEFT_INVERTED_NORMAL);
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
