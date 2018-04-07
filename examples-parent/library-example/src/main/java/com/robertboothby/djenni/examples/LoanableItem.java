package com.robertboothby.djenni.examples;

import com.robertboothby.djenni.util.Nullable;

import java.util.Date;

/**
 * <p>&#169; 2014 Forest View Developments Ltd.</p>
 *
 * @author robertboothby
 */
public interface LoanableItem <T, U extends Person> {

    public Nullable<Date> getLoanDate();

    public Nullable<Date> getReturnDate();

    public boolean isOnLoan();

    public U getBorrower();

}
