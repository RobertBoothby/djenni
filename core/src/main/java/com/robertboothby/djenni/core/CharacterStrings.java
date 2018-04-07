package com.robertboothby.djenni.core;

/**
 * This interface is intended to hold all useful character strings for construction of String and Character based
 * {@link com.robertboothby.djenni.Generator}s.
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 * @author robertboothby
 *
 * TODO add additional character set constants for other languages with associated builder methods.
 */
public interface CharacterStrings {
    /**
     * Constant string containing only English language upper case alphabetic characters.
     */
    String ENGLISH_ALPHABETIC_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * Constant string containing only English language lower case alphabetic characters.
     */
    String ENGLISH_ALPHABETIC_LOWER = "abcdefghijklmnopqrstuvwxyz";
    /**
     * Constant string containing only West Arabic Decimal Numeric characters.
     */
    String WEST_ARABIC_NUMERIC = "0123456789";
    /**
     * Constant string containing only European Decimal Numeric characters.
     */
    String EUROPEAN_NUMERIC = WEST_ARABIC_NUMERIC;
    /**
     * Constant string containing only English language lower case alphabetic and numeric characters.
     */
    String ENGLISH_ALPHANUMERIC_LOWER = ENGLISH_ALPHABETIC_LOWER + EUROPEAN_NUMERIC;
    /**
     * Constant string containing only English language upper case alphabetic and numeric characters.
     */
    String ENGLISH_ALPHANUMERIC_UPPER = ENGLISH_ALPHABETIC_UPPER + EUROPEAN_NUMERIC;
    /**
     * Constant string containing only characters used in hexadecimal numbers with lower case characters.
     */
    String HEXADECIMAL_LOWER = EUROPEAN_NUMERIC + "abdef";
    /**
     * Constant string containing only characters used in hexadecimal numbers with upper case characters.
     */
    String HEXADECIMAL_UPPER = EUROPEAN_NUMERIC + "ABCDEF";
}
