package com.robertboothby.djenni.util;

import org.junit.Test;

import static org.junit.Assert.fail;


public class SimpleMapEntryTest {

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowExceptionWhenSetCalled(){
        //Given
        SimpleMapEntry<String, String> underTest = new SimpleMapEntry<>("TEST", "TEST");

        //When
        underTest.setValue("SHOULD BREAK");

        //Then
        fail("An UnsupportedOperationException should have been thrown.");

    }
}