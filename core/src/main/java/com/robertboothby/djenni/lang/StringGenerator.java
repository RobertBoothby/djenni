package com.robertboothby.djenni.lang;

import net.jcip.annotations.ThreadSafe;
import org.djenni.Generator;
import org.hamcrest.Description;
import org.hamcrest.StringDescription;

/**
 * This Generator generates a string with defined bounds on the length and the characters used.
 * <p>
 * Marked ThreadSafe only if the internal generators are thread safe too.
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 * @author robertboothby
 */
@ThreadSafe
public class StringGenerator implements Generator<String> {

    private final Generator<Integer> lengthGenerator;
    private final Generator<Character> characterGenerator;

    /**
     * Construct an instance of the generator.
     * @param lengthGenerator a generator that will provide the length of the generated string.
     * @param characterGenerator a generator that will provide the characters composing the string.
     */
    public StringGenerator(Generator<Integer> lengthGenerator, Generator<Character> characterGenerator) {
        this.lengthGenerator = lengthGenerator;
        this.characterGenerator = characterGenerator;
    }

    /**
     * Generate a string for the given configuration.
     * @return a generated String.
     */
    public String generate() {
        int length = lengthGenerator.generate();
        char[] charBuffer = new char[length];
        for(int i = 0; i < length; i++){
            charBuffer[i] = characterGenerator.generate();
        }
        return new String(charBuffer);
    }

    public void describeTo(Description description) {
        description
                .appendText("{ StringGenerator : { lengthGenerator : ")
                .appendDescriptionOf(lengthGenerator)
                .appendText(", characterGenerator : ")
                .appendDescriptionOf(characterGenerator)
                .appendText(" } }");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StringGenerator)) return false;

        StringGenerator that = (StringGenerator) o;

        if (!characterGenerator.equals(that.characterGenerator)) return false;
        if (!lengthGenerator.equals(that.lengthGenerator)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = lengthGenerator.hashCode();
        result = 31 * result + characterGenerator.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringDescription description = new StringDescription();
        description.appendDescriptionOf(this);
        return description.toString();
    }
}
