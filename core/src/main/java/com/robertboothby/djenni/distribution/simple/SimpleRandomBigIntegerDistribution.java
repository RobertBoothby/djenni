package com.robertboothby.djenni.distribution.simple;

import org.apache.commons.lang.NotImplementedException;
import com.robertboothby.djenni.distribution.Distribution;
import org.hamcrest.Description;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 *
 * @author robertboothby
 */
public abstract class SimpleRandomBigIntegerDistribution implements Distribution<BigInteger, Double> {

    @Override
    public abstract BigInteger generate(Double bound);




    private static class SimpleRandomDoubleDistributionWrapper extends SimpleRandomBigIntegerDistribution {
        private final Distribution<Double, Double> distribution;

        private SimpleRandomDoubleDistributionWrapper(Distribution<Double, Double> distribution){

            this.distribution = distribution;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("simple random big integer distribution wrapping ");
            distribution.describeTo(description);
        }

        @Override
        public BigInteger generate(Double bound) {
            return BigDecimal.valueOf(distribution.generate(bound)).toBigInteger();
        }
    }
}
