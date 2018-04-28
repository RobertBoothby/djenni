package com.robertboothby.djenni.common;

import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.core.SupplierHelper;

import java.util.Scanner;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.robertboothby.djenni.core.SupplierHelper.derived;
import static com.robertboothby.djenni.core.SupplierHelper.fromValues;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.StreamSupport.stream;

/**
 * Builder class for a Supplier of Names.
 */
public class NameSupplierBuilder implements SupplierBuilder<Name> {

    public static final String[] DEFAULT_GIVEN_NAMES;
    public static final String[] DEFAULT_FAMILY_NAMES;

    static {
            DEFAULT_GIVEN_NAMES = readValues("/Given_Names.csv");
            DEFAULT_FAMILY_NAMES = readValues("/Family_Names.csv");
    }

    private String[] familyNames = DEFAULT_FAMILY_NAMES;
    private String[] givenNames = DEFAULT_GIVEN_NAMES;

    @Override
    public Supplier<Name> build() {
        return derived(Name::new, fromValues(givenNames), fromValues(familyNames));
    }

    public NameSupplierBuilder withFamilyNames(String ... familyNames) {
        this.familyNames = familyNames;
        return this;
    }

    public NameSupplierBuilder withGivenNames(String ... givenNames) {
        this.givenNames = givenNames;
        return this;
    }

    private static String[] readValues(String fileName) {
        try(Scanner scanner = new Scanner(NameSupplierBuilder.class.getResourceAsStream(fileName))){
            return stream(spliteratorUnknownSize(scanner, Spliterator.ORDERED), false).toArray(String[]::new);
        }
    }

    public static NameSupplierBuilder nameGenerator(){
        return new NameSupplierBuilder();
    }

    public static NameSupplierBuilder nameGenerator(Consumer<NameSupplierBuilder> consumer){
        NameSupplierBuilder builder = nameGenerator();
        consumer.accept(builder);
        return builder;
    }
}
