package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.core.CharacterStrings;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.distribution.simple.SimpleRandomDoubleDistribution;
import com.robertboothby.djenni.distribution.simple.SimpleRandomIntegerDistribution;
import com.robertboothby.djenni.sugar.And;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.robertboothby.djenni.lang.CharacterSupplierBuilder.characterSupplier;
import static com.robertboothby.djenni.lang.IntegerSupplierBuilder.integerSupplier;

/**
 * This Builder helps build Suppliers of String. It allows for the easy selection of
 * characters to use and configuration of the range of lengths of string to generate. It is not intended to provide
 * any significant support for the generation of meaningful Strings, other generators should be used or created for that
 * purpose.
 * <p>
 * @author robertboothby
 */
public class StringSupplierBuilder implements SupplierBuilder<String>, CharacterStrings {


    public static final int DEFAULT_MINIMUM_LENGTH = 6;
    private int minimumLength = DEFAULT_MINIMUM_LENGTH;
    public static final int DEFAULT_MAXIMUM_LENGTH = 12;
    private int maximumLength = DEFAULT_MAXIMUM_LENGTH;
    public static final Distribution<Integer, Integer> DEFAULT_LENGTH_DISTRIBUTION = SimpleRandomIntegerDistribution.UNIFORM;
    private Distribution<Integer, Integer> lengthDistribution = DEFAULT_LENGTH_DISTRIBUTION;
    //The default values used by the generator
    public static final String DEFAULT_AVAILABLE_CHARACTERS = ENGLISH_ALPHABETIC_UPPER;
    private String availableCharacters = DEFAULT_AVAILABLE_CHARACTERS;
    public static final Distribution<Integer, Integer> DEFAULT_CHARACTER_SELECTION_DISTRIBUTION = SimpleRandomIntegerDistribution.UNIFORM;
    private Distribution<Integer, Integer> characterSelectionDistribution = DEFAULT_CHARACTER_SELECTION_DISTRIBUTION;

    public StreamableSupplier<String> build() {
        Supplier<Integer> lengths = integerSupplier(
                builder -> builder
                        .between(minimumLength)
                        .and(maximumLength)
                        .withDistribution(lengthDistribution)
        );

        StreamableSupplier<Character> characters = characterSupplier(
                builder -> builder
                        .withCharacters(availableCharacters)
                        .withDistribution(characterSelectionDistribution)
        );
        return () -> characters.stream(lengths.get())
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
    }

    /**
     * Set the minimum and maximum lengths of the string that can be generated.
     * @param minimumLength the minimum length that can be generated.
     * @return an {@link And} that allows the maximum length to be set before returning the builder for further configuration.
     */
    public And<StringSupplierBuilder, Integer> withLengthsBetween(int minimumLength) {
        this.minimumLength = minimumLength;
        return new And<StringSupplierBuilder, Integer>() {
            public StringSupplierBuilder and(Integer maximumLength) {
                StringSupplierBuilder.this.maximumLength = maximumLength;
                return StringSupplierBuilder.this;
            }
        };
    }

    /**
     * Set the string to always be generated with a fix length.
     * @param length The length of string to be generated.
     * @return the builder for further configuration.
     */
    public StringSupplierBuilder withFixedLength(int length){
        this.minimumLength = length;
        this.maximumLength = length + 1;
        return this;
    }

    /**
     * Set the available characters to be used in generating the String.
     * @param availableCharacters the available characters used in generating the String.
     * @return the builder for further configuration.
     */
    public StringSupplierBuilder withAvailableCharacters(String availableCharacters) {
        this.availableCharacters = availableCharacters;
        return this;
    }

    /**
     * Set the available characters for the string to {@link CharacterStrings#ENGLISH_ALPHABETIC_UPPER}.
     * @return the builder for further usage.
     */
    public StringSupplierBuilder withUpperCaseEnglishAlphabet() {
        this.availableCharacters = ENGLISH_ALPHABETIC_UPPER;
        return this;
    }

    /**
     * Set the available characters for the string to {@link CharacterStrings#ENGLISH_ALPHABETIC_LOWER}.
     * @return the builder for further usage.
     */
    public StringSupplierBuilder withLowerCaseEnglishAlphabet() {
        this.availableCharacters = ENGLISH_ALPHABETIC_LOWER;
        return this;
    }

    /**
     * Set the available characters for the string to {@link CharacterStrings#EUROPEAN_NUMERIC}.
     * @return the builder for further usage.
     */
    public StringSupplierBuilder withEuropeanNumerals() {
        this.availableCharacters = EUROPEAN_NUMERIC;
        return this;
    }

    /**
     * Set the available characters for the string to {@link CharacterStrings#HEXADECIMAL_UPPER}.
     * @return the builder for further usage.
     */
    public StringSupplierBuilder withUpperCaseHexadecimal() {
        this.availableCharacters = HEXADECIMAL_UPPER;
        return this;
    }

    /**
     * Set the available characters for the string to {@link CharacterStrings#HEXADECIMAL_LOWER}.
     * @return the builder for further usage.
     */
    public StringSupplierBuilder withLowerCaseHexadecimal() {
        this.availableCharacters = HEXADECIMAL_LOWER;
        return this;
    }

    /**
     * Set the available characters for the string to {@link CharacterStrings#ENGLISH_ALPHANUMERIC_UPPER}.
     * @return the builder for further usage.
     */
    public StringSupplierBuilder withUpperCaseEnglishAlphanumeric() {
        this.availableCharacters = ENGLISH_ALPHANUMERIC_UPPER;
        return this;
    }

    /**
     * Set the available characters for the string to {@link CharacterStrings#ENGLISH_ALPHANUMERIC_LOWER}.
     * @return the builder for further usage.
     */
    public StringSupplierBuilder withEnglishLowerCaseAlphanumeric() {
        this.availableCharacters = ENGLISH_ALPHANUMERIC_LOWER;
        return this;
    }

    /**
     * Set the {@link SimpleRandomDoubleDistribution} to be used when generating the random values for selecting the length.
     *
     * @param lengthDistribution the distribution to be used when generating the random values for selecting the length.
     * @return the builder for further usage.
     */
    public StringSupplierBuilder withLengthDistribution(Distribution<Integer, Integer> lengthDistribution) {
        this.lengthDistribution = lengthDistribution;
        return this;
    }

    /**
     * Set the {@link SimpleRandomDoubleDistribution} to be used when generating the random values for selecting the characters.
     *
     * @param characterSelectionDistribution the distribution to be used when generating the random values for selecting the characters.
     * @return the builder for further usage.
     */
    public StringSupplierBuilder withCharacterSelectionDistribution(Distribution<Integer, Integer> characterSelectionDistribution) {
        this.characterSelectionDistribution = characterSelectionDistribution;
        return this;
    }

    /**
     * Get an instance of the builder for usage.
     * @return and instance of the builder for usage.
     */
    public static StringSupplierBuilder arbitraryString() {
        return new StringSupplierBuilder();
    }

    public static StreamableSupplier<String> arbitraryString(Consumer<StringSupplierBuilder> configuration) {
        StringSupplierBuilder builder = new StringSupplierBuilder();
        configuration.accept(builder);
        return builder.build();

    }
}
