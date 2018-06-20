package com.robertboothby.djenni.distribution.simple;

import com.robertboothby.djenni.distribution.Distribution;
import org.hamcrest.Description;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * This distribution generates positive BigIntegers within the range of value that can be described by a Double.
 * @author robertboothby
 */
public abstract class SimpleRandomBigIntegerDistribution implements Distribution<BigInteger, Double> {

    @Override
    public abstract BigInteger generate(Double bound);

    public static final SimpleRandomBigIntegerDistribution UNIFORM = new SimpleRandomDoubleDistributionWrapper(SimpleRandomDoubleDistribution.UNIFORM);
    public static final SimpleRandomBigIntegerDistribution NORMAL = new SimpleRandomDoubleDistributionWrapper(SimpleRandomDoubleDistribution.NORMAL);
    public static final SimpleRandomBigIntegerDistribution LEFT_NORMAL = new SimpleRandomDoubleDistributionWrapper(SimpleRandomDoubleDistribution.LEFT_NORMAL);
    public static final SimpleRandomBigIntegerDistribution RIGHT_NORMAL = new SimpleRandomDoubleDistributionWrapper(SimpleRandomDoubleDistribution.RIGHT_NORMAL);
    public static final SimpleRandomBigIntegerDistribution INVERTED_NORMAL = new SimpleRandomDoubleDistributionWrapper(SimpleRandomDoubleDistribution.INVERTED_NORMAL);
    public static final SimpleRandomBigIntegerDistribution LEFT_INVERTED_NORMAL = new SimpleRandomDoubleDistributionWrapper(SimpleRandomDoubleDistribution.LEFT_INVERTED_NORMAL);
    public static final SimpleRandomBigIntegerDistribution RIGHT_INVERTED_NORMAL = new SimpleRandomDoubleDistributionWrapper(SimpleRandomDoubleDistribution.RIGHT_INVERTED_NORMAL);

    private static class SimpleRandomDoubleDistributionWrapper extends SimpleRandomBigIntegerDistribution {
        private final Distribution<Double, Double> distribution;

        private SimpleRandomDoubleDistributionWrapper(Distribution<Double, Double> distribution){

            this.distribution = distribution;
        }
        @Override
        public BigInteger generate(Double bound) {
            return BigDecimal.valueOf(distribution.generate(bound)).toBigInteger();
        }
    }
}
