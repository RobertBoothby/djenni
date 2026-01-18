package com.robertboothby.djenni.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class SimpleMapEntryTest {

    @Test
    public void shouldThrowExceptionWhenSetCalled(){
        //Given
        SimpleMapEntry<String, String> underTest = new SimpleMapEntry<>("TEST", "TEST");

        //When
        assertThrows(UnsupportedOperationException.class, () -> underTest.setValue("SHOULD BREAK"));
    }
}
