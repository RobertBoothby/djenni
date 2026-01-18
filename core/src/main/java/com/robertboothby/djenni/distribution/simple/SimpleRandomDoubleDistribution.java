package com.robertboothby.djenni.distribution.simple;

import com.robertboothby.djenni.distribution.Distribution;

import java.io.Serializable;

import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * This Distribution is intended to provide intentionally biased or unbiased sources of randomness for testing.
 * The biases are intended to allow control of likelihood of edge or near-edge cases in order to make overflows and
 * other unexpected behaviours more likely. The biases are intended to to be close but not exact approximations of the
 * defined distributions.
 * <p>
 * This class is currently thread safe because at core it uses the {@link java.util.concurrent.ThreadLocalRandom} class
 * which removes any inter-dependencies. Overriding the seed is not supported and would make little sense in a multi-
 * threaded environment anyway. If generated values are needed for repeatable testing then they must be
 * stored independently after generation.
 *
 * @author robertboothby
 * {@link java.util.concurrent.ThreadLocalRandom#nextLong(long)}
 */
public abstract class SimpleRandomDoubleDistribution implements Serializable, Distribution<Double, Double> {

    /**
     * This constant represents the adjustment needed to the standard normal distribution to produce a reasonable
     * approximation to a normal distribution clipped between 0.0 and 1.0 with a mean of 0.5. This is achieved by using
     * modulo arrithmetic to capture the majority of the normal distribution curve - 99.9% exists in the range between
     * -3.0 and + 3.0 so to a good approximation the distribution can be captured by applying a modulo operation of 3.0
     * or greater followed by a division of the same result. The values outside the range is recaptured by the modulo
     * calculation leaving the remainder that map back into the distribution. The greater the sigma the less biassed the
     * results are but the less granularity to the numbers generated.
     */
    private static final int CORE_DISTRIBUTION_SIGMA = 4;

    /**
     * For the given distribution get the next double value with a range between 0.0 (inclusive) and 1.0 (exclusive).
     * This method is used as the source method for all other random generation methods.
     * @return the next double value between 0.0 (inclusive) and 1.0 (exclusive).
     */
    abstract double nextDouble();

    /**
      * Get the next double value within the range between 0.0 (inclusive) and the bound (exclusive).
      * @param bound the strictly positive upper bound (exclusive) on the values that can be generated.
      * @return next double value within the between 0.0 (inclusive) and the bound (exclusive).
      */
    public Double generate(Double bound){
        if (bound == null || bound <= 0.0D) {
            throw new IllegalArgumentException("Bound must be positive");
        }
        return bound * nextDouble();
    }

    /**
     * This distribution generates values that are approximately uniformly distributed within the range.
     */
    public static final SimpleRandomDoubleDistribution UNIFORM = new SimpleRandomDoubleDistribution(){
        @Override
        public double nextDouble() {
            return current().nextDouble();
        }
    };

    /**
     * This is the core distribution model for all normal distributions. representing a standard normal
     * distribution that has been clipped using modulo maths to values between -1.0 and 1.0 with a mean of 0.0.
     */
    private static final SimpleRandomDoubleDistribution CORE_NORMAL = new SimpleRandomDoubleDistribution() {

        @Override
        public double nextDouble() {
            return (current().nextGaussian() % CORE_DISTRIBUTION_SIGMA) / CORE_DISTRIBUTION_SIGMA;
        }
    };

    /**
     * This distribution generates values that are distributed in a manner that approximates the normal distribution
     * with it's mean in the middle of the range.
     */
    public static final SimpleRandomDoubleDistribution NORMAL = new SimpleRandomDoubleDistribution() {
        @Override
        public double nextDouble() {
            return (CORE_NORMAL.nextDouble() + 1.0D) / 2.0D;
        }
    };

    /**
     * This distribution generate values that are distributed in manner that approximates the right hand side of the
     * normal distribution with majority of values generated on the lower end of the range.
     */
    public static final SimpleRandomDoubleDistribution RIGHT_NORMAL = new SimpleRandomDoubleDistribution() {
        @Override
        public double nextDouble() {
            return Math.abs(CORE_NORMAL.nextDouble());
        }
    };

    /**
     * This distribution generate values that are distributed in manner that approximates the left hand side of the
     * normal distribution with majority of values generated on the upper end of the range.
     */
    public static final SimpleRandomDoubleDistribution LEFT_NORMAL = new SimpleRandomDoubleDistribution() {
        @Override
        public double nextDouble() {
            return 1.0D - RIGHT_NORMAL.nextDouble();
        }
    };

    /**
     * This function (truly terrifyingly!) approximates an inverted normal by transforming the CORE_NORMAL distribution
     * with a discontinuous translation of the X axis. Values generated by the CORE_NORMAL distribution that have value
     * less than 0.0 have 1.0 added to them whereas other values have 1.0 subtracted from them. This results in a new
     * distribution curve that very roughly looks like an inverted normal distribution, with a mean of 0.0 and the
     * majority of values clustered towards -1.0 and 1.0 with very few values around 0.0.
     */
    private static final SimpleRandomDoubleDistribution CORE_INVERTED_NORMAL = new SimpleRandomDoubleDistribution() {
        @Override
        public double nextDouble() {
            double workingCopy = CORE_NORMAL.nextDouble();
            return workingCopy < 0 ? workingCopy + 1.0D : workingCopy -1.0D;
        }
    };

    /**
     * This distribution generates values that are distributed in a manner that roughly approximates an inversion of the
     * normal distribution with it's mean in the middle of the range.
     */
    public static final SimpleRandomDoubleDistribution INVERTED_NORMAL = new SimpleRandomDoubleDistribution() {
        @Override
        public double nextDouble() {
            return (CORE_INVERTED_NORMAL.nextDouble() + 1.0D) / 2.0D;
         }
     };

    /**
     * This distribution generates values that are distributed in a manner that roughly approximates the right hand side
     * of an inversion of the normal distribution with the majority of values generated at the upper end of the range.
     */
    public static final SimpleRandomDoubleDistribution RIGHT_INVERTED_NORMAL = new SimpleRandomDoubleDistribution() {
        @Override
        public double nextDouble() {
            return Math.abs(CORE_INVERTED_NORMAL.nextDouble());
        }
    };

    /**
     * This distribution generates values that are distributed in a manner that roughly approximates the left hand side
     * of an inversion of the normal distribution with the majority of values generated at the lower end of the range.
     */
    public static final SimpleRandomDoubleDistribution LEFT_INVERTED_NORMAL = new SimpleRandomDoubleDistribution() {
        @Override
        public double nextDouble() {
            return 1.0D - RIGHT_INVERTED_NORMAL.nextDouble();
        }
    };


}
