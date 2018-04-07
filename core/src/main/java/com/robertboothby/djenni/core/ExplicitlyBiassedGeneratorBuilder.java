package com.robertboothby.djenni.core;

import com.robertboothby.djenni.SerializableGenerator;
import com.robertboothby.djenni.SerializableGeneratorBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.robertboothby.djenni.core.ExplicitlyBiasedGenerator.BiasDetail;
import static com.robertboothby.djenni.core.ExplicitlyBiasedGenerator.biasDetail;

/**
 * TODO Implement...
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 *
 * @author robertboothby
 */
public class ExplicitlyBiassedGeneratorBuilder<T> implements SerializableGeneratorBuilder<T> {

    /**
     * A default weight of 1.0D.
     */
    public static final Double DEFAULT_WEIGHT = 1.0D;

    private List<ExplicitlyBiasedGenerator.BiasDetail<T>> biaslist = new ArrayList<>();

    /**
     * Add (or update) a value with the {@link #DEFAULT_WEIGHT}.
     * @param value The value to add.
     * @return the builder for further configuration.
     */
    public ExplicitlyBiassedGeneratorBuilder<T> addValue(T value) {
        biaslist.add(ExplicitlyBiasedGenerator.biasDetail(value, DEFAULT_WEIGHT));
        return this;
    }

    /**
     * Add (or update) a value with the passed in weight.
     * @param value The value to add.
     * @param weight the weight of the value being added.
     * @return the builder for further configuration.
     */
    public ExplicitlyBiassedGeneratorBuilder<T> addValue(T value, double weight) {
        biaslist.add(ExplicitlyBiasedGenerator.biasDetail(value, weight));
        return this;
    }

    /**
     * Add (or update) multiple values with the {@link #DEFAULT_WEIGHT}.
     * @param values the values to add.
     * @return the builder for further configuration.
     */
    public ExplicitlyBiassedGeneratorBuilder<T> addValues(T ... values) {
        for (T value : values){
            biaslist.add(ExplicitlyBiasedGenerator.biasDetail(value, DEFAULT_WEIGHT));
        }
        return this;
    }

    /**
     * Add (or update) multiple values with the passed in weight.
     * @param values the values to add.
     * @param weight the weight of the values being added.
     * @return the builder for further configuration.
     */
    public ExplicitlyBiassedGeneratorBuilder<T> addValues(double weight, T ... values) {
        for (T value : values){
            biaslist.add(ExplicitlyBiasedGenerator.biasDetail(value, weight));
        }
        return this;
    }

    public SerializableGenerator<T> build() {
        return new ExplicitlyBiasedGenerator<T>(biaslist);
    }

    /**
     * Get a new instance of the builder to be configured.
     * @param <T> The type of object that will be generated by the configured generators.
     * @return a new instance of the builder.
     */
    public static <T> ExplicitlyBiassedGeneratorBuilder<T> explicitlyBiassedGeneratorFor(Class<T> classType) {
        return new ExplicitlyBiassedGeneratorBuilder<T>();
    }
}
