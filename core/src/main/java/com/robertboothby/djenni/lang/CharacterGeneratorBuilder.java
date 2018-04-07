package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.distribution.simple.SimpleRandomIntegerDistribution;
import net.jcip.annotations.NotThreadSafe;
import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.SerializableGenerator;
import com.robertboothby.djenni.SerializableGeneratorBuilder;
import com.robertboothby.djenni.core.CharacterStrings;
import com.robertboothby.djenni.distribution.simple.SimpleRandomIntegerDistribution;

import static java.util.Arrays.copyOf;
import static com.robertboothby.djenni.core.GeneratorHelper.buildAn;
import static com.robertboothby.djenni.core.GeneratorHelper.fixedValue;
import static com.robertboothby.djenni.lang.IntegerGeneratorBuilder.integerGenerator;

/**
 * Builder intended to make it expressive and easy to configure a Generator of Integers.
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 * @author robertboothby
 */
@NotThreadSafe
public class CharacterGeneratorBuilder implements SerializableGeneratorBuilder<Character>, CharacterStrings {

    //The default values used by the generator
    public static final String DEFAULT_AVAILABLE_CHARACTERS = ENGLISH_ALPHABETIC_UPPER;
    public static final Distribution<Integer, Integer> DEFAULT_CHARACTER_SELECTION_DISTRIBUTION = SimpleRandomIntegerDistribution.UNIFORM;

    private char[] characters = DEFAULT_AVAILABLE_CHARACTERS .toCharArray();
    private Distribution<Integer, Integer> distribution = DEFAULT_CHARACTER_SELECTION_DISTRIBUTION;

    public SerializableGenerator<Character> build() {
        if(characters.length == 1){
            return fixedValue(characters[0]);
        } else if (characters.length > 1) {
            return new RandomCharacterGenerator(characters,
                    buildAn(integerGenerator().between(0).and(characters.length).withDistribution(distribution)));
        } else {
            throw new IllegalArgumentException("There must be some characters to generate.");
        }
    }

    public CharacterGeneratorBuilder withCharacters(char[] characters){
        this.characters = copyOf(characters, characters.length);
        return this;
    }

    public CharacterGeneratorBuilder withCharacters(String characterString) {
        this.characters = characterString.toCharArray();
        return this;
    }

    public CharacterGeneratorBuilder withDistribution(Distribution<Integer, Integer> distribution) {
        this.distribution = distribution;
        return this;
    }

    public static CharacterGeneratorBuilder characterGenerator(){
        return new CharacterGeneratorBuilder();
    }
}
