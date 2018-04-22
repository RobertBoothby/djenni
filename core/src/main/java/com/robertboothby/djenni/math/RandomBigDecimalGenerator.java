package com.robertboothby.djenni.math;

import com.robertboothby.djenni.distribution.Distribution;
import org.apache.commons.lang3.NotImplementedException;
import org.hamcrest.Description;

import java.math.BigDecimal;

/**
 * FROM STACK OVERFLOW
 * http://stackoverflow.com/questions/2290057/how-to-generate-a-random-biginteger-value-in-java
 *
 * @author robertboothby
 */
public class RandomBigDecimalGenerator implements Distribution<BigDecimal, BigDecimal> {

    public BigDecimal generate(BigDecimal bound) {
        //TODO Implement...
        throw new NotImplementedException("Needs implementation.");
    }

    public void describeTo(Description description) {
        throw new NotImplementedException("Needs implementation.");
    }
}
