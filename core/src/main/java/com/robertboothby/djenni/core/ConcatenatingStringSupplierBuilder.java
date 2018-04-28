package com.robertboothby.djenni.core;

import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.sugar.And;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * TODO describe
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

    @Override
    public Supplier<String> build() {
        //Take a copy of the list so that it won't be mutated.
        List<Supplier<?>> generators = new ArrayList<>(this.suppliers);
        return () -> generators.stream().map(Supplier::get).map(Object::toString).collect(Collectors.joining());
    }

}
