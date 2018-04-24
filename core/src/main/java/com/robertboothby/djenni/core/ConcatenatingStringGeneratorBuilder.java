package com.robertboothby.djenni.core;

import com.robertboothby.djenni.Generator;
import com.robertboothby.djenni.GeneratorBuilder;
import com.robertboothby.djenni.sugar.And;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.robertboothby.djenni.core.LambdaGenerator.generator;

/**
 * TODO describe
 */
public class ConcatenatingStringGeneratorBuilder implements GeneratorBuilder<String>, And<ConcatenatingStringGeneratorBuilder, Generator<?>> {

    private List<Generator<?>> generators = new ArrayList<>();

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
    public ConcatenatingStringGeneratorBuilder with(Generator<?> value) {
        generators.add(value);
        return this;
    }

    /**
     * Add the generators the the set of generators in the concatenation.
     * @param values the values to add.
     * @return the buider for further configuration.
     */
    public ConcatenatingStringGeneratorBuilder with(Generator<?> ... values) {
        Collections.addAll(generators, values);
        return this;
    }

    /**
     * Add a generator the the set of generators in the concatenation.
     * @param value the value to add.
     * @return the buider for further configuration.
     */
    public ConcatenatingStringGeneratorBuilder and(Generator<?> value) {
        return with(value);
    }

    /**
     * Add the generators the the set of generators in the concatenation.
     * @param values the values to add.
     * @return the buider for further configuration.
     */
    public ConcatenatingStringGeneratorBuilder and(Generator<?> ...  values) {
        return with(values);
    }

    @Override
    public Generator<String> build() {
        //Take a copy of the list so that it won't be mutated.
        List<Generator<?>> generators = new ArrayList<>(this.generators);
        return generator(
                () -> generators.stream().map(Generator::generate).map(Object::toString).collect(Collectors.joining()),
                d -> d
                        .appendText("Concatenating String Generator : { ")
                        .appendList("[",", ", "]",  generators)
                        .appendText(" }")
        );
    }

}
