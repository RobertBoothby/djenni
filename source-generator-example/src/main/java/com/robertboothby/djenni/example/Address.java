package com.robertboothby.djenni.example;

import java.util.ArrayList;
import java.util.List;

public class Address {

    private final List<String> addressLines = new ArrayList<>();
    private final Country country;
    private final Postcode postcode;
    private String region;

    public Address(Country country, Postcode postcode) {
        this.country = country;
        this.postcode = postcode;
    }

    public List<String> getAddressLines() {
        return addressLines;
    }

    public Country getCountry() {
        return country;
    }

    public Postcode getPostcode() {
        return postcode;

    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressLines=" + addressLines +
                ", country=" + country +
                ", postcode=" + postcode +
                ", region='" + region + '\'' +
                '}';
    }
}
