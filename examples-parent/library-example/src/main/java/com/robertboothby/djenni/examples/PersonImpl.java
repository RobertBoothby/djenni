package com.robertboothby.djenni.examples;

/**
 * <p>&#169; 2014 Forest View Developments Ltd.</p>
 *
 * @author robertboothby
 */
public class PersonImpl implements Person {
    private String firstName;
    private String lastName;
    private String SSN;
    private Gender gender;

    public PersonImpl(String firstName, String lastName, String SSN, Gender gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.SSN = SSN;
        this.gender = gender;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getSSN() {
        return SSN;
    }

    @Override
    public boolean isSamePerson(Person person) {
        return this.equals(person);
    }

    @Override
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonImpl person = (PersonImpl) o;

        if (!SSN.equals(person.SSN)) return false;
        if (!firstName.equals(person.firstName)) return false;
        if (gender != person.gender) return false;
        if (!lastName.equals(person.lastName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + SSN.hashCode();
        result = 31 * result + gender.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PersonImpl{");
        sb.append("firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", SSN='").append(SSN).append('\'');
        sb.append(", gender=").append(gender);
        sb.append('}');
        return sb.toString();
    }
}
