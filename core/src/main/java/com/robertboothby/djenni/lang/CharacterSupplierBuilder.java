package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.core.CharacterStrings;
import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.distribution.simple.SimpleRandomIntegerDistribution;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.robertboothby.djenni.core.SupplierHelper.buildAn;
import static com.robertboothby.djenni.core.SupplierHelper.fix;
import static com.robertboothby.djenni.lang.IntegerSupplierBuilder.integerSupplier;
import static java.util.Arrays.copyOf;

/**
 * Builder intended to make it expressive and easy to configure a Supplier of Characters.
 * @author robertboothby
 */
public class CharacterSupplierBuilder implements SupplierBuilder<Character>, CharacterStrings {

    //The default values used by the generator
    public static final String DEFAULT_AVAILABLE_CHARACTERS = ENGLISH_ALPHABETIC_UPPER;
    public static final Distribution<Integer, Integer> DEFAULT_CHARACTER_SELECTION_DISTRIBUTION = SimpleRandomIntegerDistribution.UNIFORM;

    private char[] characters = DEFAULT_AVAILABLE_CHARACTERS .toCharArray();
    private Distribution<Integer, Integer> distribution = DEFAULT_CHARACTER_SELECTION_DISTRIBUTION;

    public Supplier<Character> build() {
        if(characters.length == 1){
            return fix(characters[0]);
        } else if (characters.length > 1) {
            char[] charactersCopy = Arrays.copyOf(characters, characters.length);
            Supplier<Integer> positionGenerator = buildAn(
                    integerSupplier()
                            .between(0)
                            .and(characters.length)
                            .withDistribution(distribution));
            return () -> charactersCopy[positionGenerator.get()];
        } else {
            throw new IllegalArgumentException("There must be some characters to generate.");
        }
    }

    public CharacterSupplierBuilder withCharacters(char[] characters){
        this.characters = copyOf(characters, characters.length);
        return this;
    }

    public CharacterSupplierBuilder withCharacters(String characterString) {
        this.characters = characterString.toCharArray();
        return this;
    }

    public CharacterSupplierBuilder withDistribution(Distribution<Integer, Integer> distribution) {
        this.distribution = distribution;
        return this;
    }

    public static CharacterSupplierBuilder characterSupplier(){
        return new CharacterSupplierBuilder();
    }

    public static Supplier<Character> characterSupplier(Consumer<CharacterSupplierBuilder> consumer){
        CharacterSupplierBuilder builder = new CharacterSupplierBuilder();
        consumer.accept(builder);
        return builder.build();
    }

}
