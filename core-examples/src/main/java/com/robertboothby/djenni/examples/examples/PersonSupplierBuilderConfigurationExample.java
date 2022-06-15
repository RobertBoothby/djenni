package com.robertboothby.djenni.examples.examples;

import com.robertboothby.djenni.common.Name;
import com.robertboothby.djenni.common.NameSupplierBuilder;
import com.robertboothby.djenni.core.CachingSupplier;
import com.robertboothby.djenni.core.StreamableSupplier;

import java.time.Instant;

import static com.robertboothby.djenni.core.SupplierHelper.afterGetCalled;
import static com.robertboothby.djenni.examples.examples.PersonSupplierBuilder.aPerson;
import static com.robertboothby.djenni.lang.LongSupplierBuilder.generateALong;

public class PersonSupplierBuilderConfigurationExample {
    public StreamableSupplier<Person> inlineConfig() {
        //Provide a default configuration immediately.
        PersonSupplierBuilder personSupplierBuilder = aPerson($ ->
                //Let's now constrain the date of birth to between the start of the epoch and now.
                $.setTimeOfBirthSupplier(
                        generateALong()
                                .between(0L)
                                .and(System.currentTimeMillis())
                                .build()
                                .derive(Instant::ofEpochMilli)
                )
        );
        return personSupplierBuilder.build();
    }

    public StreamableSupplier<Person> postFactConfig() {
        PersonSupplierBuilder personSupplierBuilder = aPerson();
        //...
        //Provide a configuration after the fact.
        personSupplierBuilder.configure($ -> $.setTimeOfBirthSupplier(
                generateALong()
                        .between(0L)
                        .and(System.currentTimeMillis())
                        .build()
                        .derive(Instant::ofEpochMilli)
        ));
        return personSupplierBuilder.build();
    }

    public StreamableSupplier<Person> postFactSet() {
        PersonSupplierBuilder personSupplierBuilder = aPerson();
        //...
        //Configure the builder directly.
        personSupplierBuilder
                .setTimeOfBirthSupplier(
                        generateALong()
                                .between(0L)
                                .and(System.currentTimeMillis())
                                .build()
                                .derive(Instant::ofEpochMilli)
                );
        return personSupplierBuilder.build();
    }

    public StreamableSupplier<Person> inlineConfig2() {
        //Provide a default configuration immediately.
        return aPerson(this::epochBirthdate).build();
    }

    public StreamableSupplier<Person> postFactConfig2() {
        PersonSupplierBuilder personSupplierBuilder = aPerson();
        //...
        //Provide a configuration after the fact.
        personSupplierBuilder.configure(this::epochBirthdate);
        return personSupplierBuilder.build();
    }

    public StreamableSupplier<Person> postFactSet2() {
        PersonSupplierBuilder personSupplierBuilder = aPerson();
        //...
        //Configure the builder directly.
        epochBirthdate(aPerson());
        return personSupplierBuilder.build();
    }

    private PersonSupplierBuilder epochBirthdate(PersonSupplierBuilder $) {
        return $.setTimeOfBirthSupplier(
                generateALong()
                        .between(0L)
                        .and(System.currentTimeMillis())
                        .build()
                        .derive(Instant::ofEpochMilli)
        );
    }

    public StreamableSupplier<Person> CachingSupplierConfig() {
        //Provide a default configuration immediately.
        CachingSupplier<Name> cachingNameSupplier = CachingSupplier.cacheSuppliedValues(NameSupplierBuilder.names());
        StreamableSupplier<Person> personStreamableSupplier = aPerson(this::epochBirthdate)
                .setGivenNamesSupplier(cachingNameSupplier
                        .derive(Name::getGivenName)
                        .derive(givenName -> new String[]{givenName}))
                .setFamilyNameSupplier(cachingNameSupplier
                        .derive(Name::getFamilyName))
                .build();
        //Ensure that the caching name supplier is updated after every call to get a person.
        return afterGetCalled(personStreamableSupplier, ignored -> cachingNameSupplier.next());
    }
}
