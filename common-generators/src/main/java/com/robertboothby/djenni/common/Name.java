package com.robertboothby.djenni.common;

import com.robertboothby.djenni.Generator;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;
import java.util.function.Function;

/**
 * This class exists to provide a neutral representation of the name being generated. It is expected that the NameGenerator
 * that uses it will be wrapped to provide a more domain specific generator perhaps using
 * {@link com.robertboothby.djenni.core.GeneratorHelper#derived(Function, Generator)}.
 */
public class Name {

    private final String givenName;
    private final String familyName;

    /**
     * Construct an instance of the name.
     * @param givenName The given name.
     * @param familyName The family name.
     */
    public Name(String givenName, String familyName) {
        this.givenName = givenName;
        this.familyName = familyName;
    }

    /**
     * Get the given name.
     * @return The given name.
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * Get the family name.
     * @return the family name,
     */
    public String getFamilyName() {
        return familyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name = (Name) o;
        return Objects.equals(givenName, name.givenName) &&
                Objects.equals(familyName, name.familyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(givenName, familyName);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("givenName", givenName)
                .append("familyName", familyName)
                .toString();
    }
}
