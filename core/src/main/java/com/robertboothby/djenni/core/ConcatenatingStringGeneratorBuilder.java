package com.robertboothby.djenni.core;

import com.robertboothby.djenni.Generator;
import com.robertboothby.djenni.GeneratorBuilder;
import com.robertboothby.djenni.sugar.And;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>&#169; 2014 Forest View Developments Ltd.</p>
 *
 * @author robertboothby
 */
public class ConcatenatingStringGeneratorBuilder implements GeneratorBuilder<String>, And<ConcatenatingStringGeneratorBuilder, Generator> {

    private List<Generator<String>> generators = new ArrayList<>();

    public static ConcatenatingStringGeneratorBuilder generatorOfConcatenatedValues() {
        return new ConcatenatingStringGeneratorBuilder();
    }

    public ConcatenatingStringGeneratorBuilder with(Generator value) {
        generators.add(value);
        return this;
    }

    @Override
    public ConcatenatingStringGeneratorBuilder and(Generator value) {
        generators.add(value);
        return this;
    }


    @Override
    public Generator<String> build() {
        return new ConcatenatingStringGenerator(generators);
    }
}
