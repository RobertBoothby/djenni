package com.robertboothby.djenni.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.function.Supplier;

import static com.robertboothby.djenni.core.SupplierHelper.fix;
import static com.robertboothby.djenni.util.SimpleMapEntry.mapEntry;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(MockitoExtension.class)
public class SimpleMapSupplierBuilderTest {

    @Test
    public void shouldCreateAMap(){
        //Given
        Supplier<Map<String,String>> mapSupplier = SimpleMapSupplierBuilder.<String, String>mapSupplierBuilder().build($ -> $
                .withNumberOfEntries(fix(1))
                .withEntries(fix(mapEntry("TEST", "TEST")))
        );

        //When
        Map<String, String> result = mapSupplier.get();

        //Then
        assertThat(result, hasEntry("TEST", "TEST"));
        assertThat(result.entrySet(), hasSize(1));
    }

    @Test
    public void builtSimpleMapSuppliersShouldRemainStableAfterBuilderChanges() {
        SimpleMapSupplierBuilder<String, String> builder = SimpleMapSupplierBuilder.<String, String>mapSupplierBuilder()
                .withNumberOfEntries(fix(1))
                .withEntries(fix(mapEntry("FIRST", "FIRST")));

        Supplier<Map<String, String>> supplier = builder.build();

        builder.withEntries(fix(mapEntry("SECOND", "SECOND")));

        Map<String, String> map = supplier.get();
        assertThat(map, hasEntry("FIRST", "FIRST"));
        assertThat(map.entrySet(), hasSize(1));
    }

}
