package com.robertboothby.djenni.example;

import com.robertboothby.djenni.SupplierBuilder;
import org.junit.Test;

import java.util.function.Supplier;

import static com.robertboothby.djenni.core.SupplierHelper.fromEnum;
import static com.robertboothby.djenni.example.PostcodeSupplierBuilder.postcodeSupplier;
import static com.robertboothby.djenni.lang.StringSupplierBuilder.arbitraryString;
import static com.robertboothby.djenni.util.SimpleListSupplierBuilder.simpleList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class TestAddressSupplierBuilder {
    @Test
    public void shouldGenerateAddressOnceConfigured(){
        //Given
        Supplier<Address> addressSupplier = AddressSupplierBuilder.addressSupplier(address ->
                address
                        .withCountryConstructor(fromEnum(Country.class))
                        .withPostcodeConstructor(
                                postcodeSupplier(
                                        generator -> generator
                                                .withPostcodeConstructor(exactly10Text())
                                )
                        )
                        .withRegionSetter(arbitraryString())
                        .withAddressLinesCollection(
                                simpleList(list ->
                                        list.withEntrySupplier(arbitraryString())
                                                .withSizeBetween(3)
                                                .and(5)
                                )
                        )
        );

        //When
        Address address = addressSupplier.get();

        //Then

        assertThat(address.getCountry(), notNullValue());
        assertThat(address.getPostcode().getPostcode().length(), is(10));

        System.out.println(address.toString());

    }

    private SupplierBuilder<String> exactly10Text(){
        return arbitraryString().withFixedLength(10);
    }

}
