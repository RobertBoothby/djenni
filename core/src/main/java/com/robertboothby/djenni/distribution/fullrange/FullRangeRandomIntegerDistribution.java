package com.robertboothby.djenni.distribution.fullrange;

import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.distribution.simple.SimpleRandomLongDistribution;
import org.hamcrest.Description;

/**
 * This Distribution allows for the generation of the full range of integer values, positive to negative.
 * @author robertboothby
 */
public class FullRangeRandomIntegerDistribution implements Distribution<Integer, Long> {

    private final SimpleRandomLongDistribution underlyingDistribution;
    public static final long MAX_BOUND = 0x100000000L;

    public static final FullRangeRandomIntegerDistribution UNIFORM =
            new FullRangeRandomIntegerDistribution(SimpleRandomLongDistribution.UNIFORM);
    public static final FullRangeRandomIntegerDistribution NORMAL =
            new FullRangeRandomIntegerDistribution(SimpleRandomLongDistribution.NORMAL);
    public static final FullRangeRandomIntegerDistribution LEFT_NORMAL =
            new FullRangeRandomIntegerDistribution(SimpleRandomLongDistribution.LEFT_NORMAL);
    public static final FullRangeRandomIntegerDistribution RIGHT_NORMAL =
            new FullRangeRandomIntegerDistribution(SimpleRandomLongDistribution.RIGHT_NORMAL);
    public static final FullRangeRandomIntegerDistribution INVERTED_NORMAL =
            new FullRangeRandomIntegerDistribution(SimpleRandomLongDistribution.INVERTED_NORMAL);
    public static final FullRangeRandomIntegerDistribution LEFT_INVERTED_NORMAL =
            new FullRangeRandomIntegerDistribution(SimpleRandomLongDistribution.LEFT_INVERTED_NORMAL);
    public static final FullRangeRandomIntegerDistribution RIGHT_INVERTED_NORMAL =
            new FullRangeRandomIntegerDistribution(SimpleRandomLongDistribution.RIGHT_INVERTED_NORMAL);

    public FullRangeRandomIntegerDistribution(SimpleRandomLongDistribution underlyingDistribution) {
        this.underlyingDistribution = underlyingDistribution;
    }

    public Integer generate(Long bound) {
        //TODO check bound is in range...
        //TODO rethink how to express bound and relationship to generating negative values as this interface makes it
        // hard to determin what range of values will be generated. May want to create a "Bound" type and use that
        // instead of using "Number".
        return (int) underlyingDistribution.generate(bound).longValue();
    }

    public void describeTo(Description description) {
        description
                .appendText("asIntegerDistribution { ")
                .appendDescriptionOf(underlyingDistribution)
                .appendText(" } ");
    }

    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.MIN_VALUE);
        System.out.println((long) Integer.MAX_VALUE - (long) Integer.MIN_VALUE + 1);
        System.out.println(MAX_BOUND);
    }
}
