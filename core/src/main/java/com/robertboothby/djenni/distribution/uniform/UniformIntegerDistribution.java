package com.robertboothby.djenni.distribution.uniform;

import org.apache.commons.lang.NotImplementedException;
import com.robertboothby.djenni.distribution.Distribution;
import org.hamcrest.Description;

import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 *
 * @author robertboothby
 * @deprecated
 */
public class UniformIntegerDistribution implements Distribution<Integer, Integer> {
    public Integer generate(Integer bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }

    public void describeTo(Description description) {
        //TODO Implement...
        throw new NotImplementedException();
    }
}
