package com.robertboothby.djenni.example;

import org.junit.Test;

import static com.robertboothby.djenni.core.GeneratorHelper.fromEnum;
import static com.robertboothby.djenni.example.PostcodeGeneratorBuilder.postcodeGenerator;
import static com.robertboothby.djenni.lang.StringGeneratorBuilder.generatorOfArbitraryStrings;
import static com.robertboothby.djenni.util.SimpleListGeneratorBuilder.simpleListGenerator;

public class TestAddressGeneratorBuilder {
    @Test
    public void shouldGenerateAddressOnceConfigured(){
        //Given
        AddressGeneratorBuilder addressGeneratorBuilder = AddressGeneratorBuilder.addressGenerator(address ->
                address
                        .withCountryConstructorGenerator(fromEnum(Country.class))
                        .withRegionSetterGenerator(
                                generatorOfArbitraryStrings().withEnglishLowerCaseAlphanumeric()
                        )
                        .withAddressLinesCollectionGenerator(
                                simpleListGenerator(list ->
                                        list.withEntryGenerator(generatorOfArbitraryStrings().withEnglishLowerCaseAlphanumeric())
                                                .withSize()
                                                .between(3)
                                                .and(5)
                                )
                        )
                        .withPostcodeConstructorGenerator(
                                postcodeGenerator(
                                        generator -> generator
                                                .withPostcodeConstructorGenerator(generatorOfArbitraryStrings()
                                                        .withAFixedLengthOf(10)
                                                )
                                )
                        )
        );
        //When

        //Then

    }
}
