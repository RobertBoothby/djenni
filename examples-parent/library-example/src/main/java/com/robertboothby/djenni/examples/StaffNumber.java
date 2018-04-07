package com.robertboothby.djenni.examples;

import java.util.regex.Pattern;

/**
 * <p>&#169; 2014 Forest View Developments Ltd.</p>
 *
 * @author robertboothby
 */
public class StaffNumber {

    private final String stringStaffNumber;
    public static final Pattern STAFF_NUMBER_PATTERN = Pattern.compile("[A-Z]{3}[0-9]{4}");

    public StaffNumber(String stringStaffNumber) {
        if(validateStaffNumberFormat(stringStaffNumber)) {
            this.stringStaffNumber = stringStaffNumber;
        } else {
            throw new IllegalArgumentException("Invalid staff number format");
        }
    }

    public String getStringStaffNumber() {
        return stringStaffNumber;
    }

    private static boolean validateStaffNumberFormat(String candidateNumber) {
        return STAFF_NUMBER_PATTERN.matcher(candidateNumber).matches();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StaffNumber that = (StaffNumber) o;

        if (!stringStaffNumber.equals(that.stringStaffNumber)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return stringStaffNumber.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StaffNumber{");
        sb.append("stringStaffNumber='").append(stringStaffNumber).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
