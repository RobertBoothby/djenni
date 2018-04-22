package com.robertboothby.djenni.common;

import com.robertboothby.djenni.Generator;
import org.hamcrest.Description;

/**
 * Simple name generator.
 */
public class NameGenerator implements Generator<Name> {

    private final Generator<String> givenNameGenerator;
    private final Generator<String> familyNameGenerator;

    public NameGenerator(Generator<String> givenNameGenerator, Generator<String> familyNameGenerator) {
        this.givenNameGenerator = givenNameGenerator;
        this.familyNameGenerator = familyNameGenerator;
    }

    @Override
    public Name generate() {
        return new Name(givenNameGenerator.generate(), familyNameGenerator.generate());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("GivenNameGenerator : {\n");
        description.appendText("givenNameGenerator : { ").appendDescriptionOf(givenNameGenerator).appendText("},\n");
        description.appendText("familyNameGenerator : {").appendDescriptionOf(familyNameGenerator).appendText("}\n");
        description.appendText("}\n");
    }
}
