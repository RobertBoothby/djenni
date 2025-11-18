package com.robertboothby.djenni.core;

import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.distribution.simple.SimpleRandomDoubleDistribution;

import java.util.List;
import java.util.TreeMap;
import java.util.function.Supplier;

/**
 * Supplies values according to explicitly configured weights. The builder flattens the weights into a cumulative
 * probability map and a uniform {@link Distribution} is then used to select the next entry. Weights can be any positive
 * {@code double}; zero or negative weights are ignored. The underlying distribution defaults to
 * {@link SimpleRandomDoubleDistribution#UNIFORM}, so the relative proportions remain stable regardless of the absolute
 * values used.
 *
 * <p>For example:</p>
 * <pre>{@code
 * List<ExplicitlyBiassedSupplier.BiasDetail<Boolean>> biasList = List.of(
 *         ExplicitlyBiassedSupplier.biasDetail(() -> Boolean.TRUE, 0.9D),
 *         ExplicitlyBiassedSupplier.biasDetail(() -> Boolean.FALSE, 0.1D)
 * );
 * StreamableSupplier<Boolean> supplier = new ExplicitlyBiassedSupplier<>(biasList);
 * }</pre>
 * <p>supplies {@code true} roughly 90% of the time and {@code false} the rest.</p>
 */
public class ExplicitlyBiassedSupplier<T> implements StreamableSupplier<T> {

    protected final TreeMap<Double, Supplier<T>> lookupMap;
    protected final double proportionsTotal;
    protected final Distribution<Double, Double> distribution = SimpleRandomDoubleDistribution.UNIFORM;

    /**
     * Creates a supplier from precomputed bias details. The list is iterated in order and converted into a cumulative
     * lookup map, so the supplied list should be deterministic to guarantee reproducible proportions across runs.
     * @param biasList bias configuration describing which supplier to invoke for each weighted bucket
     */
    public ExplicitlyBiassedSupplier(List<BiasDetail<T>> biasList) {
        this.lookupMap = new TreeMap<>();
        double runningProportionsTotal = 0.0D;
        for(BiasDetail<T> biasDetail : biasList){
            runningProportionsTotal += biasDetail.biasProportion;
            if(biasDetail.biasProportion > 0.0D) { //If someone has slipped in a 0.0D value ignore it otherwise we get an issue.
                lookupMap.put(runningProportionsTotal, biasDetail.biasedValue);
            }
        }
        this.proportionsTotal = runningProportionsTotal;
    }

    /**
     * Generate the next value using the configured weights.
     * @return the biased value chosen for the randomly generated position
     */
    public T get() {
        Double mapKeyValue = distribution.generate(proportionsTotal);
        return lookupMap.ceilingEntry(mapKeyValue).getValue().get();
    }

    /**
     * Class used in the construction of the ExplicitlyBiassedSupplier.
     * @param <T> The type of object to be included in the bias list.
     */
    static class BiasDetail<T> {
        private final double biasProportion;
        private final Supplier<T> biasedValue;

        private BiasDetail(Supplier<T> biasedValue, Double biasProportion) {
            this.biasedValue = biasedValue;
            this.biasProportion = biasProportion;
        }
    }

    /**
     * Convenience factory used by builders. Negative or zero biases are accepted but ignored by the constructor, so the
     * onus is on callers to supply sensible weights.
     */
    static <T> BiasDetail<T> biasDetail(Supplier<T> biasedValue, double biasProportion) {
        return new BiasDetail<T>(biasedValue, biasProportion);
    }
}
