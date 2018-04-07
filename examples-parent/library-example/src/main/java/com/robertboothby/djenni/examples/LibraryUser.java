package com.robertboothby.djenni.examples;

import java.util.Collection;

/**
 * <p>&#169; 2014 Forest View Developments Ltd.</p>
 *
 * @author robertboothby
 */
public class LibraryUser implements Person {

    private PersonImpl identity;
    private LibraryUserNumber libraryUserNumber;
    private Collection<Loan<Book>> booksOnLoan;

    @Override
    public String getFirstName() {
        return identity.getFirstName();
    }

    @Override
    public String getLastName() {
        return identity.getLastName();
    }

    @Override
    public String getSSN() {
        return identity.getSSN();
    }

    @Override
    public Gender getGender() {
        return identity.getGender();
    }

    @Override
    public boolean isSamePerson(Person person) {
        return identity.isSamePerson(person);
    }
}
