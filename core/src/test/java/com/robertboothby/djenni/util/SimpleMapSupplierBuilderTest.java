package com.robertboothby.djenni.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;
import java.util.function.Supplier;

import static com.robertboothby.djenni.core.SupplierHelper.fix;
import static com.robertboothby.djenni.util.SimpleMapEntry.mapEntry;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SimpleMapSupplierBuilderTest {

    @Test
    public void shouldCreateAMap(){
        //Given
        Supplier<Map<String,String>> mapSupplier = SimpleMapSupplierBuilder.map($ -> $
                .withNumberOfEntries(fix(1))
                .withEntries(fix(mapEntry("TEST", "TEST")))
        );

        //When
        Map<String, String> result = mapSupplier.get();

        //Then
        assertThat(result, hasEntry("TEST", "TEST"));
        assertThat(result.entrySet(), hasSize(1));
    }

}