package com.robertboothby.djenni.examples;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.lang.StringSupplierBuilder;

import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.robertboothby.djenni.lang.LongSupplierBuilder.generateALong;
import static com.robertboothby.djenni.lang.StringSupplierBuilder.arbitraryString;
import static com.robertboothby.djenni.util.SimpleListSupplierBuilder.simpleList;

public class PersonSupplierBuilder implements ConfigurableSupplierBuilder<Person, PersonSupplierBuilder> {
    private Supplier<String[]> givenNamesSupplier;
    private Supplier<String> familyNameSupplier;
    private Supplier<Instant> timeOfBirthSupplier;
    private Supplier<List<String>> titlesByPrecedenceSupplier;

    public PersonSupplierBuilder() {
        StreamableSupplier<String> arbitraryStrings = arbitraryString().build();
        StreamableSupplier<Long> arbitraryLongs = generateALong().build();
        givenNamesSupplier = () -> arbitraryStrings.stream(1).toArray(String[]::new);
        familyNameSupplier = arbitraryStrings;
        timeOfBirthSupplier = () -> Instant.ofEpochMilli(arbitraryLongs.get());
        titlesByPrecedenceSupplier = simpleList($ -> $.entries(arbitraryStrings).withSizeBetween(1).and(3));
    }

    @Override
    public StreamableSupplier<Person> build() {
        //Effectively final local variables to isolate the generated supplier from any changes to the builder.
        Supplier<String[]> givenNamesSupplier = this.givenNamesSupplier;
        Supplier<String> familyNameSupplier = this.familyNameSupplier;
        Supplier<Instant> timeOfBirthSupplier = this.timeOfBirthSupplier;
        Supplier<List<String>> titlesByPrecedenceSupplier = this.titlesByPrecedenceSupplier;
        return () -> new Person(
                givenNamesSupplier.get(),
                familyNameSupplier.get(),
                timeOfBirthSupplier.get(),
                titlesByPrecedenceSupplier.get()
        );
    }

    public PersonSupplierBuilder setGivenNamesSupplier(Supplier<String[]> givenNamesSupplier) {
        this.givenNamesSupplier = givenNamesSupplier;
        return this;
    }

    public PersonSupplierBuilder setFamilyNameSupplier(Supplier<String> familyNameSupplier) {
        this.familyNameSupplier = familyNameSupplier;
        return this;
    }

    public PersonSupplierBuilder setTimeOfBirthSupplier(Supplier<Instant> timeOfBirthSupplier) {
        this.timeOfBirthSupplier = timeOfBirthSupplier;
        return this;
    }

    public PersonSupplierBuilder setTitlesByPrecedenceSupplier(Supplier<List<String>> titlesByPrecedenceSupplier) {
        this.titlesByPrecedenceSupplier = titlesByPrecedenceSupplier;
        return this;
    }

    public static PersonSupplierBuilder aPerson(){
        return new PersonSupplierBuilder();
    }

    public static PersonSupplierBuilder aPerson(Consumer<PersonSupplierBuilder> initialConfig){
        return new PersonSupplierBuilder().configure(initialConfig);
    }
}
