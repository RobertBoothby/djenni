package com.robertboothby.djenni.core;

import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.sugar.And;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.lang.String.join;
import static java.util.stream.Collectors.toList;

/**
 * This SupplierBuilder builds a Supplier that concatenates the {@link Object#toString()}} results of multiple other
 * Suppliers together to supply a String.
 */
public class ConcatenatingStringSupplierBuilder implements SupplierBuilder<String> {

    private List<Supplier<?>> suppliers = new ArrayList<>();

    /**
     * Get an instance of this builder.
     * @return an instance of the builder.
     */
    public static ConcatenatingStringSupplierBuilder supplierOfConcatenatedValues() {
        return new ConcatenatingStringSupplierBuilder();
    }

    /**
     * Get an instance of this supplier configured using this builder.
     * @param configuration the configuration to use.
     * @return an instance of the builder.
     */
    public static StreamableSupplier<String> supplierOfConcatenatedValues(Consumer<ConcatenatingStringSupplierBuilder> configuration) {
        ConcatenatingStringSupplierBuilder builder = supplierOfConcatenatedValues();
        configuration.accept(builder);
        return builder.build();
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
     * Add the suppliers the the set of suppliers in the concatenation.
     * @param values the values to add.
     * @return the builder for further configuration.
     */
    public ConcatenatingStringSupplierBuilder and(Supplier<?> ...  values) {
        return with(values);
    }

    /**
     * Adds the suppliers as a delimited list with starting and ending elements.
     * @param start The supplier for values that go at the start of the list.
     * @param end The supplier for values that go at the end of the list.
     * @param delimiter The supplier for the delimiters.
     * @param values The suppliers of values in the list.
     * @return The builder for further configuration.
     */
    public ConcatenatingStringSupplierBuilder withList(Supplier<?> start, Supplier<?> end, Supplier<?> delimiter, Supplier<? extends List<?>> values) {
        suppliers.add(start);
        withList(delimiter, values);
        suppliers.add(end);
        return this;
    }

    /**
     * Adds the suppliers as a delimited list.
     * @param delimiter The supplier for the delimiters.
     * @param values The suppliers of values in the list.
     * @return the builder for further configuration.
     */
    public ConcatenatingStringSupplierBuilder withList(Supplier<?> delimiter, Supplier<? extends List<?>> values) {
        //Make a supplier that does the joining for us.
        this.suppliers.add(
                () -> join(
                        delimiter.get().toString(),
                        values.get().stream()
                                .map(Object::toString)
                                .collect(toList())));
        return this;
    }

    @Override
    public StreamableSupplier<String> build() {
        //Take a copy of the list so that it won't be mutated.
        List<Supplier<?>> generators = new ArrayList<>(this.suppliers);
        return () -> generators.stream().map(Supplier::get).map(Object::toString).collect(Collectors.joining());
    }

}
