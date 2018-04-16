package com.robertboothby.djenni.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Address {

    private final List<String> addressLines = new ArrayList<>();
    private final Country country;
    private final Postcode postcode;

    public Address(Country country, Postcode postcode, String ... addressLines) {
        this.country = country;
        this.postcode = postcode;
        this.addressLines.addAll(Arrays.asList(addressLines));
    }

    public List<String> getAddressLines() {
        return Collections.unmodifiableList(addressLines);
    }

    public Country getCountry() {
        return country;
    }

    public Postcode getPostcode() {
        return postcode;
    }
}
