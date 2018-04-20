package com.robertboothby.djenni.example;

import com.robertboothby.djenni.Generator;
import com.robertboothby.djenni.GeneratorBuilder;
import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static com.robertboothby.djenni.core.GeneratorHelper.fromEnum;
import static com.robertboothby.djenni.example.PostcodeGeneratorBuilder.postcodeGenerator;
import static com.robertboothby.djenni.lang.StringGeneratorBuilder.arbitraryString;
import static com.robertboothby.djenni.util.SimpleListGeneratorBuilder.simpleList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class TestAddressGeneratorBuilder {
    @Test
    public void shouldGenerateAddressOnceConfigured(){
        //Given
        AddressGeneratorBuilder addressGeneratorBuilder = AddressGeneratorBuilder.addressGenerator(address ->
                address
                        .withCountryConstructor(fromEnum(Country.class))
                        .withPostcodeConstructor(
                                postcodeGenerator(
                                        generator -> generator
                                                .withPostcodeConstructor(exactly10Text())
                                )
                        )
                        .withRegionSetter(arbitraryString())
                        .withAddressLinesCollection(
                                simpleList(list ->
                                        list.withEntryGenerator(arbitraryString())
                                                .withSizeBetween(3)
                                                .and(5)
                                )
                                        )
        );
        Generator<Address> addressGenerator = addressGeneratorBuilder.build();
        Description description = new StringDescription();
        addressGenerator.describeTo(description);
        System.out.println(description.toString());

        //When
        Address address = addressGenerator.generate();

        //Then

        assertThat(address.getCountry(), notNullValue());
        assertThat(address.getPostcode().getPostcode().length(), is(10));

        System.out.println(address.toString());

    }

    private GeneratorBuilder<String> exactly10Text(){
        return arbitraryString().withFixedLength(10);
    }

}
