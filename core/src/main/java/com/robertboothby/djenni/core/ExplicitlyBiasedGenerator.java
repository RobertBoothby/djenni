package com.robertboothby.djenni.core;

import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.distribution.simple.SimpleRandomDoubleDistribution;
import org.apache.commons.lang.NotImplementedException;
import com.robertboothby.djenni.SerializableGenerator;
import com.robertboothby.djenni.distribution.Distribution;
import org.hamcrest.Description;

import java.util.List;
import java.util.TreeMap;

import static com.robertboothby.djenni.distribution.simple.SimpleRandomDoubleDistribution.UNIFORM;

/**
 * <p>Instances of this class generate random values in given relative proportions that are passed in as a bias map
 * containing the allowed values as keys and the relative proportions as Double values.</p>
 * <p>For Example:
 * <code>
 *     List&lt;ExplicitlyBiasedGenerator.BiasEntry&lt;Boolean&gt;&gt; biasList = new ArrayList&lt;&gt;();
 *     biasList.add(Boolean.TRUE, 0.9D);
 *     biasMap.put(Boolean.FALSE, 0.1D);
 * </code>
 * Would produce a bias map that when passed into this class would generate TRUE 90% of the time and FALSE 10% of the
 * time.</p>
 *
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 *
 * @author robertboothby
 */
public class ExplicitlyBiasedGenerator<T> implements SerializableGenerator<T> {

    private final TreeMap<Double, T> lookupMap;
    private final double proportionsTotal;
    private final Distribution<Double, Double> distribution = SimpleRandomDoubleDistribution.UNIFORM;

    /**
     * Construct an instance of the generator using the map of values with their biasses
     * @param biasList a list of {@link com.robertboothby.djenni.core.ExplicitlyBiasedGenerator.BiasDetail} that supplies the entries
     *                 and relative proportions of the entries for generation.
     */
    public ExplicitlyBiasedGenerator(List<BiasDetail<T>> biasList) {
        this.lookupMap = new TreeMap<>();
        double runningProportionsTotal = 0.0D;
        for(BiasDetail<T> biasDetail : biasList){
            runningProportionsTotal += biasDetail.biasProportion;
            lookupMap.put(runningProportionsTotal, biasDetail.biasedValue);
        }
        this.proportionsTotal = runningProportionsTotal;
    }

    public T generate() {
        Double mapKeyValue = distribution.generate(proportionsTotal);
        return lookupMap.ceilingEntry(mapKeyValue).getValue();
    }

    public void describeTo(Description description) {
        throw new NotImplementedException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExplicitlyBiasedGenerator that = (ExplicitlyBiasedGenerator) o;

        return      Double.compare(that.proportionsTotal, proportionsTotal) == 0
                &&  distribution.equals(that.distribution)
                &&  lookupMap.equals(that.lookupMap);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = lookupMap.hashCode();
        temp = Double.doubleToLongBits(proportionsTotal);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + distribution.hashCode();
        return result;
    }

    /**
     * Class used in the construction of the ExplicitlyBiasedGenerator.
     * @param <T> The type of object to be included in the bias list.
     */
    public static class BiasDetail<T> {
        private final double biasProportion;
        private final T biasedValue;

        private BiasDetail(T biasedValue, Double biasProportion) {
            this.biasedValue = biasedValue;
            this.biasProportion = biasProportion;
        }
    }

    public static <T> BiasDetail<T> biasDetail(T biasedValue, double biasProportion) {
        return new BiasDetail<T>(biasedValue, biasProportion);
    }
}
