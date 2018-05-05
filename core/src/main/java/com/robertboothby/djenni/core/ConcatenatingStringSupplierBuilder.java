package com.robertboothby.djenni.core;

import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.sugar.And;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * This SupplierBuilder builds a Supplier that concatenates the {@link Object#toString()}} results of multiple other
 * Suppliers together to supply a String.
 */
public class ConcatenatingStringSupplierBuilder implements SupplierBuilder<String>, And<ConcatenatingStringSupplierBuilder, Supplier<?>> {

    private List<Supplier<?>> suppliers = new ArrayList<>();

    /**
     * Get an instance of this builder.
     * @return an instance of the builder.
     */
    public static ConcatenatingStringSupplierBuilder supplierOfConcatenatedValues() {
        return new ConcatenatingStringSupplierBuilder();
    }

    /**
     * Add a supplier the the set of suppliers in the concatenation.
     * @param value the value to add.
     * @return the buider for further configuration.
     */
    public ConcatenatingStringSupplierBuilder with(Supplier<?> value) {
        suppliers.add(value);
        return this;
    }

    /**
     * Add the suppliers the the set of suppliers in the concatenation.
     * @param values the values to add.
     * @return the buider for further configuration.
     */
    public ConcatenatingStringSupplierBuilder with(Supplier<?> ... values) {
        Collections.addAll(suppliers, values);
        return this;
    }

    /**
     * Add a generator the the set of suppliers in the concatenation.
     * @param value the value to add.
     * @return the buider for further configuration.
     */
    public ConcatenatingStringSupplierBuilder and(Supplier<?> value) {
        return with(value);
    }

    /**
     * Add the suppliers the the set of suppliers in the concatenation.
     * @param values the values to add.
     * @return the buider for further configuration.
     */
    public ConcatenatingStringSupplierBuilder and(Supplier<?> ...  values) {
        return with(values);
    }

    public ConcatenatingStringSupplierBuilder with(Supplier<?> start, Supplier<?> end, Supplier<?> separator, List<Supplier<?>> values) {
        suppliers.add(start);
        with(separator, values);
        suppliers.add(end);
        return this;
    }

    public ConcatenatingStringSupplierBuilder with(Supplier<?> separator, List<Supplier<?>> suppliers) {
        boolean notFirst = false;
        for (Supplier<?> supplier : suppliers) {
            if(notFirst){
                suppliers.add(separator);
                notFirst = true;
            }
            this.suppliers.add(supplier);
        }
        return this;
    }

    @Override
    public Supplier<String> build() {
        //Take a copy of the list so that it won't be mutated.
        List<Supplier<?>> generators = new ArrayList<>(this.suppliers);
        return () -> generators.stream().map(Supplier::get).map(Object::toString).collect(Collectors.joining());
    }

}
