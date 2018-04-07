package com.robertboothby.djenni.distribution.uniform;

import org.apache.commons.lang.NotImplementedException;
import org.djenni.distribution.Distribution;
import org.hamcrest.Description;

import java.math.BigInteger;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.djenni.sugar.EasyCompare.$;

/**
 * <p>Implementation of an uniform BigInteger distribution. This class will generate BigIntegers of an arbitrary size as
 * BigInteger itself can express an arbitrary size.</p>
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 *
 * @author robertboothby
 * @deprecated
 */
public class UniformBigIntegerDistribution implements Distribution<BigInteger, BigInteger> {

    public BigInteger generate(BigInteger bound) {
        if ($(bound).lessThan(BigInteger.ONE)) {
            throw new IllegalArgumentException("The bound should be 1 or greater.");
        }
        BigInteger r;
        do {
            r = new BigInteger(bound.bitLength(), current());
        } while (r.compareTo(bound) >= 0);
        return r;
    }

    public void describeTo(Description description) {
        //TODO Implement...
        throw new NotImplementedException();
    }
}
