package com.robertboothby.djenni.examples.examples;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class Person {
    private final String[] givenNames;
    private String familyName;
    private final Instant timeOfBirth;
    private final List<String> titlesByPrecedence;

    public Person(String[] givenNames, String familyName, Instant timeOfBirth, List<String> titlesByPrecedence) {
        this.givenNames = givenNames;
        this.familyName = familyName;
        this.timeOfBirth = timeOfBirth;
        this.titlesByPrecedence = titlesByPrecedence;
    }

    public String[] getGivenNames() {
        return givenNames;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public Instant getTimeOfBirth() {
        return timeOfBirth;
    }

    public List<String> getTitlesByPrecedence() {
        return titlesByPrecedence;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Person{");
        sb.append("givenNames=").append(Arrays.toString(givenNames));
        sb.append(", familyName='").append(familyName).append('\'');
        sb.append(", timeOfBirth=").append(timeOfBirth);
        sb.append(", titlesByPrecedence=").append(titlesByPrecedence);
        sb.append('}');
        return sb.toString();
    }
}