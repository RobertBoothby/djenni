package com.robertboothby.djenni.examples;

import com.robertboothby.djenni.util.Nullable;
import org.apache.commons.lang.NotImplementedException;
import com.robertboothby.djenni.util.Nullable;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.robertboothby.djenni.util.Nullable.nullValue;

/**
 * <p>&#169; 2014 Forest View Developments Ltd.</p>
 *
 * @author robertboothby
 */
public class Book implements LoanableItem {

    private final String title;
    private final Category primaryCategory;
    private final Set<Category> subCategories = new HashSet<Category>();
    private final List<Author> authors;

    private Nullable<LoanDetails> loanDetails = Nullable.nullValue();


    public Book(String title, Category primaryCategory, Set<Category> subCategories, List<Author> authors) {
        this.title = title;
        this.primaryCategory = primaryCategory;
        this.subCategories.addAll(subCategories);
        this.authors = authors;
    }


    @Override
    public Nullable<Date> getLoanDate() {
        //TODO Implement...
        throw new NotImplementedException();
    }

    @Override
    public Nullable<Date> getReturnDate() {
        //TODO Implement...
        throw new NotImplementedException();
    }

    @Override
    public boolean isOnLoan() {
        //TODO Implement...
        throw new NotImplementedException();
    }
}
