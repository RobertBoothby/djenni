package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.GeneratorBuilder;
import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.distribution.simple.SimpleRandomDoubleDistribution;
import com.robertboothby.djenni.Generator;
import com.robertboothby.djenni.GeneratorBuilder;
import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.distribution.simple.SimpleRandomDoubleDistribution;

/**
 * Trivial implementation of the {@link GeneratorBuilder} inteface that is created to retain consistency with the other
 * primitive types. When explicit control is required then it is better to consider using the
 * {@link com.robertboothby.djenni.core.ExplicitlyBiasedGenerator}.
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 *
 * @author robertboothby
 */
public class BooleanGeneratorBuilder implements GeneratorBuilder<Boolean> {

    private static final Distribution<Double, Double> DISTRIBUTION_DEFAULT = SimpleRandomDoubleDistribution.UNIFORM;
    private Distribution<Double, Double> distribution = DISTRIBUTION_DEFAULT;

    public Generator<Boolean> build() {
        return new RandomBooleanGenerator(distribution);
    }

    /**
     * Configure the distribution to be used with the generators that this will build.
     * @param distribution the distribution to use in any generators built.
     * @return the Generator builder for further configuration.
     */
    public BooleanGeneratorBuilder withDistribution(Distribution<Double, Double> distribution) {
        this.distribution = distribution;
        return this;
    }
}
