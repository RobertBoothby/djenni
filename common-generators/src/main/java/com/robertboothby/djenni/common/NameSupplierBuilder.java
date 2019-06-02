package com.robertboothby.djenni.common;

import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;

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

    /**
     * An array containing the default given name list from Given_Names.csv.
     */
    public static final String[] DEFAULT_GIVEN_NAMES;

    /**
     * An array containing the default family name list from Family_Names.csv.
     */
    public static final String[] DEFAULT_FAMILY_NAMES;

    //Initialise the static final name arrays.
    static {
            DEFAULT_GIVEN_NAMES = readValuesAsLinesFromAFile("/Given_Names.csv");
            DEFAULT_FAMILY_NAMES = readValuesAsLinesFromAFile("/Family_Names.csv");
    }

    private String[] familyNames = DEFAULT_FAMILY_NAMES;
    private String[] givenNames = DEFAULT_GIVEN_NAMES;

    @Override
    public StreamableSupplier<Name> build() {
        return derived(Name::new, fromValues(givenNames), fromValues(familyNames));
    }

    /**
     * Set the family names to be used in the supplier of names.
     * @param familyNames The family names to be used.
     * @return The builder for further configuration.
     */
    public NameSupplierBuilder withFamilyNames(String ... familyNames) {
        this.familyNames = familyNames;
        return this;
    }

    /**
     * Set the given names to be used in the supplier of names.
     * @param givenNames The given names to be used.
     * @return The builder for further configuration.
     */
    public NameSupplierBuilder withGivenNames(String ... givenNames) {
        this.givenNames = givenNames;
        return this;
    }


    private static String[] readValuesAsLinesFromAFile(String fileName) {
        try(Scanner scanner = new Scanner(NameSupplierBuilder.class.getResourceAsStream(fileName))){
            return stream(spliteratorUnknownSize(scanner, Spliterator.ORDERED), false).toArray(String[]::new);
        }
    }

    /**
     * Convenience method to get a builder instance with default configuration.
     * @return A new instance of NameSupplierBuilder.
     */
    public static NameSupplierBuilder nameSupplierBuilder(){
        return new NameSupplierBuilder();
    }

    /**
     * Convenience method to get a builder instance with the desired configuration.
     * @param configuration a consumer that applies the configuration to the newly created builder.
     * @return the newly created, configured builder.
     */
    public static NameSupplierBuilder nameSupplierBuilder(Consumer<NameSupplierBuilder> configuration){
        NameSupplierBuilder builder = nameSupplierBuilder();
        configuration.accept(builder);
        return builder;
    }

    /**
     * Convenience method to get a Supplier instance with default configuration.
     * @return A new Name Supplier with the desired configuration.
     */
    public static Supplier<Name> names(){
        return nameSupplierBuilder().build();
    }

    /**
     * Convenience method to get a Supplier instance with the desired configuration.
     * @param configuration a consumer that applies the configuration to the builder used to create the Supplier.
     * @return the newly created Name Supplier.
     */
    public static Supplier<Name> names(Consumer<NameSupplierBuilder> configuration){
        return nameSupplierBuilder(configuration).build();
    }
}
