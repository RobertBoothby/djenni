package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.Generator;
import org.hamcrest.Description;

/**
 * This generator provides a mechanism for generating random {@link Boolean} values. It is about the simplest of all
 * {@link Generator} implementations as it is really only a shim.
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 * @author robertboothby
 */
public class RandomBooleanGenerator implements Generator<Boolean> {

    private final Distribution<Double, Double> distribution;

    /**
     * Construct an instance of with a given distribution between true and false.
     * @param distribution the distribution to use when generating boolean values.
     */
    public RandomBooleanGenerator(Distribution<Double, Double> distribution) {
        this.distribution = distribution;
    }

    public Boolean generate() {
        return distribution.generate(1.0D) < 0.5D;
    }

    public void describeTo(Description description) {
        description
                .appendText("{ RandomBooleanGenerator : { distribution : ")
                .appendDescriptionOf(distribution)
                .appendText(" } }");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RandomBooleanGenerator that = (RandomBooleanGenerator) o;

        if (!distribution.equals(that.distribution)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return distribution.hashCode();
    }
}
