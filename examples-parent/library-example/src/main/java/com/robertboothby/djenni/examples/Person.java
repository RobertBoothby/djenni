package com.robertboothby.djenni.examples;

/**
 * <p>&#169; 2014 Forest View Developments Ltd.</p>
 *
 * @author robertboothby
 */
public interface Person {
    String getFirstName();

    String getLastName();

    String getSSN();

    public Gender getGender();

    boolean isSamePerson(Person person);
}
