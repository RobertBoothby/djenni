package com.robertboothby.djenni.common;

/**
 * This class exists to provide a neutral representation of the name being generated. It is expected that the NameGenerator
 * that uses it will be wrapped to provide a more domain specific generator.
 */
public class Name {

    private final String givenName;
    private final String familyName;


    public Name(String givenName, String familyName) {
        this.givenName = givenName;
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }
}
