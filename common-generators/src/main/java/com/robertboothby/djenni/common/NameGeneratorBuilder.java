package com.robertboothby.djenni.common;

import com.robertboothby.djenni.Generator;
import com.robertboothby.djenni.GeneratorBuilder;
import com.robertboothby.djenni.core.GeneratorHelper;

import java.util.Scanner;
import java.util.Spliterator;
import java.util.function.Consumer;

import static com.robertboothby.djenni.core.GeneratorHelper.derived;
import static com.robertboothby.djenni.core.GeneratorHelper.fromArray;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.StreamSupport.stream;

/**
 * Builder class for NameGenerator.
 */
public class NameGeneratorBuilder implements GeneratorBuilder<Name> {

    public static final String[] DEFAULT_GIVEN_NAMES;
    public static final String[] DEFAULT_FAMILY_NAMES;

    static {
            DEFAULT_GIVEN_NAMES = readValues("/Given_Names.csv");
            DEFAULT_FAMILY_NAMES = readValues("/Family_Names.csv");
    }

    private String[] familyNames = DEFAULT_FAMILY_NAMES;
    private String[] givenNames = DEFAULT_GIVEN_NAMES;

    @Override
    public Generator<Name> build() {
        return derived(Name::new, fromArray(givenNames), fromArray(familyNames));
    }

    public NameGeneratorBuilder withFamilyNames(String ... familyNames) {
        this.familyNames = familyNames;
        return this;
    }

    public NameGeneratorBuilder withGivenNames(String ... givenNames) {
        this.givenNames = givenNames;
        return this;
    }

    private static String[] readValues(String fileName) {
        try(Scanner scanner = new Scanner(NameGeneratorBuilder.class.getResourceAsStream(fileName))){
            return stream(spliteratorUnknownSize(scanner, Spliterator.ORDERED), false).toArray(String[]::new);
        }
    }

    public static NameGeneratorBuilder nameGenerator(){
        return new NameGeneratorBuilder();
    }

    public static NameGeneratorBuilder nameGenerator(Consumer<NameGeneratorBuilder> consumer){
        NameGeneratorBuilder builder = nameGenerator();
        consumer.accept(builder);
        return builder;
    }
}
