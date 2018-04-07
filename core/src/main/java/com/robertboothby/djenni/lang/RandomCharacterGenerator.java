package com.robertboothby.djenni.lang;

import org.djenni.SerializableGenerator;
import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;

import java.util.Arrays;

/**
 * TODO Document properly.
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 * @author robertboothby
 */
public class RandomCharacterGenerator implements SerializableGenerator<Character>, SelfDescribing {

    private final char[] characters;
    private final SerializableGenerator<Integer> positionGenerator;

    public RandomCharacterGenerator(char[] characters, SerializableGenerator<Integer> positionGenerator) {
        this.characters = characters;
        this.positionGenerator = positionGenerator;
    }

    public Character generate() {
        return characters[positionGenerator.generate()];
    }

    public void describeTo(Description description) {
        description
                .appendText("{ RandomCharacterGenerator : { characters : ")
                .appendValue(characters)
                .appendText(", positionGenerator : ")
                .appendDescriptionOf(positionGenerator)
                .appendText(" } }");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RandomCharacterGenerator that = (RandomCharacterGenerator) o;

        if (!Arrays.equals(characters, that.characters)) return false;
        if (!positionGenerator.equals(that.positionGenerator)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(characters);
        result = 31 * result + positionGenerator.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringDescription description = new StringDescription();
        description.appendDescriptionOf(this);
        return description.toString();
    }

}
