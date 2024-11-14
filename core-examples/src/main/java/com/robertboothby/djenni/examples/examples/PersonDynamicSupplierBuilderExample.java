package com.robertboothby.djenni.examples.examples;

import com.robertboothby.djenni.dynamic.DynamicSupplierBuilder;

import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.robertboothby.djenni.common.NameSupplierBuilder.familyNames;
import static com.robertboothby.djenni.common.NameSupplierBuilder.givenNames;
import static com.robertboothby.djenni.core.SupplierHelper.arrays;
import static com.robertboothby.djenni.core.SupplierHelper.fix;
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

    public static void main(String[] args) {
        PersonDynamicSupplierBuilderExample personDynamicSupplierBuilderExample = new PersonDynamicSupplierBuilderExample();
        DynamicSupplierBuilder<Person> personDynamicSupplierBuilder = personDynamicSupplierBuilderExample.defaultPersonDynamicSupplierBuilder();
        System.out.println(personDynamicSupplierBuilder.build().stream(10).map(Objects::toString).collect(Collectors.joining("\n")));
    }
}
