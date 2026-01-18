package com.robertboothby.djenni.examples.examples;

import com.robertboothby.djenni.dynamic.DynamicSupplierBuilder;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.robertboothby.djenni.common.NameSupplierBuilder.familyNames;
import static com.robertboothby.djenni.common.NameSupplierBuilder.givenNames;
import static com.robertboothby.djenni.core.SupplierHelper.arrays;
import static com.robertboothby.djenni.core.SupplierHelper.fix;
import static com.robertboothby.djenni.examples.examples.Person.person;
import static com.robertboothby.djenni.lang.IntegerSupplierBuilder.integerSupplier;
import static com.robertboothby.djenni.time.InstantSupplierBuilder.anInstant;

public class PersonDynamicSupplierBuilderExample {

    /**
     * Provides a pre-configured DynamicSupplierBuilder for a Person with between 1 and 5 random given names, a random family name, and a random date of birth.
     * @return a pre-configured DynamicSupplierBuilder for a Person.
     */
    public DynamicSupplierBuilder<Person> defaultPersonDynamicSupplierBuilder(){
            return DynamicSupplierBuilder.supplierFor(Person.class)
                    .property(Person::getGivenNames,
                            arrays(
                                    givenNames(),
                                    integerSupplier()
                                            .between(1)
                                            .and(6)
                                            .build(),
                                    String[]::new
                            )
                    )
                    .property(Person::getFamilyName, familyNames())
                    .property(Person::getTimeOfBirth, anInstant().build())
                    .property(Person::getTitlesByPrecedence, fix(Collections.EMPTY_LIST));
    }

    /**
     * Provides a pre-configured DynamicSupplierBuilder for a Person with between 1 and 5 random given names, a random family name, and a random date of birth.
     * @return a pre-configured DynamicSupplierBuilder for a Person.
     */
    public DynamicSupplierBuilder<Person> defaultPersonDynamicSupplierBuilderUsingConstructorSelectionSyntax(){
        return DynamicSupplierBuilder.supplierFor(Person.class)
                .useConstructor($ -> new Person($.p(String[].class), $.p(String.class), $.p(Instant.class), $.p(List.class)))
                .property(Person::getGivenNames,
                        arrays(
                                givenNames(),
                                integerSupplier()
                                        .between(1)
                                        .and(6)
                                        .build(),
                                String[]::new
                        )
                )
                .property(Person::getFamilyName, familyNames())
                .property(Person::getTimeOfBirth, anInstant().build())
                .property(Person::getTitlesByPrecedence, fix(Collections.EMPTY_LIST));
    }

    /**
     * Provides a pre-configured DynamicSupplierBuilder for a Person with between 1 and 5 random given names, a random family name, and a random date of birth.
     * @return a pre-configured DynamicSupplierBuilder for a Person.
     */
    public DynamicSupplierBuilder<Person> defaultPersonDynamicSupplierBuilderUsingFunction(){
        return DynamicSupplierBuilder.supplierFor(Person.class)
                .useFunction($ -> {
                    //Do whatever pre-requisites you need to create the object.
                    return person($.p("givenNames", new String[]{"John", "James"}), $.p("surname", String.class), $.p("timeOfBirth", Instant.class), $.p("titlesByPrecedence", List.class));
                })
                .property("surname", familyNames())
                .property(Person::getTimeOfBirth, anInstant().build())
                .property(Person::getTitlesByPrecedence, fix(Collections.EMPTY_LIST));
    }


    public static void main(String[] args) {
        PersonDynamicSupplierBuilderExample personDynamicSupplierBuilderExample = new PersonDynamicSupplierBuilderExample();
        DynamicSupplierBuilder<Person> personDynamicSupplierBuilder = personDynamicSupplierBuilderExample.defaultPersonDynamicSupplierBuilderUsingFunction();
        System.out.println(personDynamicSupplierBuilder.build().stream(10).map(Objects::toString).collect(Collectors.joining("\n")));
    }
}
