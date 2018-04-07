package com.robertboothby.djenni.distribution.simple;

import org.djenni.distribution.Distribution;
import org.hamcrest.Description;

/**
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 *
 * @author robertboothby
 */
public class SimpleRandomIntegerDistribution implements Distribution<Integer, Integer> {

    private final SimpleRandomDoubleDistribution underlyingDistribution;

    public static final SimpleRandomIntegerDistribution UNIFORM =
            new SimpleRandomIntegerDistribution(SimpleRandomDoubleDistribution.UNIFORM);
    public static final SimpleRandomIntegerDistribution NORMAL =
            new SimpleRandomIntegerDistribution(SimpleRandomDoubleDistribution.NORMAL);
    public static final SimpleRandomIntegerDistribution LEFT_NORMAL =
            new SimpleRandomIntegerDistribution(SimpleRandomDoubleDistribution.LEFT_NORMAL);
    public static final SimpleRandomIntegerDistribution RIGHT_NORMAL =
            new SimpleRandomIntegerDistribution(SimpleRandomDoubleDistribution.RIGHT_NORMAL);
    public static final SimpleRandomIntegerDistribution INVERTED_NORMAL =
            new SimpleRandomIntegerDistribution(SimpleRandomDoubleDistribution.INVERTED_NORMAL);
    public static final SimpleRandomIntegerDistribution LEFT_INVERTED_NORMAL =
            new SimpleRandomIntegerDistribution(SimpleRandomDoubleDistribution.LEFT_INVERTED_NORMAL);
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
