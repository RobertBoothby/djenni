package com.robertboothby.djenni.core;

import com.robertboothby.djenni.Generator;
import com.robertboothby.djenni.GeneratorBuilder;
import com.robertboothby.djenni.sugar.And;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Builder for the {@link ConcatenatingStringGenerator}.
 */
public class ConcatenatingStringGeneratorBuilder implements GeneratorBuilder<String>, And<ConcatenatingStringGeneratorBuilder, Generator> {

    private List<Generator<? extends Object>> generators = new ArrayList<>();

    /**
     * Get an instance of this builder.
     * @return an instance of the builder.
     */
    public static ConcatenatingStringGeneratorBuilder generatorOfConcatenatedValues() {
        return new ConcatenatingStringGeneratorBuilder();
    }

    /**
     * Add a generator the the set of generators in the concatenation.
     * @param value the value to add.
     * @return the buider for further configuration.
     */
    public ConcatenatingStringGeneratorBuilder with(Generator value) {
        generators.add(value);
        return this;
    }

    /**
     * Add the generators the the set of generators in the concatenation.
     * @param values the values to add.
     * @return the buider for further configuration.
     */
    public ConcatenatingStringGeneratorBuilder with(Generator ... values) {
        Collections.addAll(generators, values);
        return this;
    }

    /**
     * Add a generator the the set of generators in the concatenation.
     * @param value the value to add.
     * @return the buider for further configuration.
     */
    public ConcatenatingStringGeneratorBuilder and(Generator value) {
        return with(value);
    }

    /**
     * Add the generators the the set of generators in the concatenation.
     * @param values the values to add.
     * @return the buider for further configuration.
     */
    public ConcatenatingStringGeneratorBuilder and(Generator ...  values) {
        return with(values);
    }

    @Override
    public Generator<String> build() {
        return new ConcatenatingStringGenerator(generators);
    }
}
