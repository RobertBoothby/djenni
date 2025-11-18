package com.robertboothby.djenni.core;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.lang.String.join;
import static java.util.stream.Collectors.toList;

/**
 * Builder for {@link StreamableSupplier}s that concatenate the {@link Object#toString()} representation of multiple
 * other suppliers. Suppliers are invoked in the order they are registered and the builder captures the supplier list
 * at build time so subsequent builder changes cannot affect an already-built supplier. Unless a specific list variant
 * is requested the resulting string contains no delimiters or separators—callers must opt-in via the {@code withList}
 * helpers if they need them.
 */
public class ConcatenatingStringSupplierBuilder implements ConfigurableSupplierBuilder<String, ConcatenatingStringSupplierBuilder> {

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
     * Append one or more suppliers to the concatenation chain. Each supplier contributes the result of calling
     * {@link Object#toString()} on the value it produces, and the suppliers are invoked for every call to the built
     * supplier so they should be side-effect free if ordering matters.
     * @param values the suppliers whose values should be appended in order
     * @return the builder for further configuration
     */
    public ConcatenatingStringSupplierBuilder with(Supplier<?> ... values) {
        Collections.addAll(suppliers, values);
        return this;
    }

    /**
     * Synonym for {@link #with(Supplier[])} that can be used to produce fluent, English-like configuration.
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
     * Adds the suppliers as a delimited list. Both the delimiter supplier and the list supplier are invoked during
     * every call to the built supplier, so they can themselves be dynamic generators (for example random lists or
     * locale-sensitive delimiters).
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
