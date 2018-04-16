package com.robertboothby.djenni.example;

/**
 * Example class that will potentially support regular expression validation that will
 * allow the creation of a regular expression generator.
 */
public class Postcode {
    private final String postcode;


    public Postcode(String postcode) {
        this.postcode = postcode;
    }

    public String getPostcode() {
        return postcode;
    }
}
