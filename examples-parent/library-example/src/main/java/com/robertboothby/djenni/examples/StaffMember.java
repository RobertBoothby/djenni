package com.robertboothby.djenni.examples;

import org.apache.commons.lang.NotImplementedException;

/**
 * <p>&#169; 2014 Forest View Developments Ltd.</p>
 *
 * @author robertboothby
 */
public class StaffMember implements Person {

    private Person identity;
    private StaffNumber staffNumber;

    public StaffMember(Person identity, StaffNumber staffNumber) {
        this.identity = identity;
        this.staffNumber = staffNumber;
    }

    public StaffNumber getStaffNumber() {
        return staffNumber;
    }

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
