package com.robertboothby.djenni.util;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.robertboothby.djenni.core.SupplierHelper.fix;
import static com.robertboothby.djenni.lang.IntegerSupplierBuilder.integerSupplier;
import static com.robertboothby.djenni.util.MapSupplierHelper.supplyEntries;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;

public class MapSupplierBuilderTest {

    @Test
    public void shouldBuildHashMap(){
        //Given
        SimpleMapSupplierBuilder<Integer, String> mapSupplierBuilder =
                SimpleMapSupplierBuilder.<Integer, String>map()
                        .withEntries(
                                supplyEntries(
                                        integerSupplier()
                                                .between(1)
                                                .and(6)
                                                .sequential()
                                                .build(),
                                        Object::toString)
                        )
                        .withNumberOfEntries(fix(5)
                        );
        //When
        Map<Integer, String> actual = mapSupplierBuilder.build().get();

        //Then
        assertThat(actual, hasEntry(1, "1"));
        assertThat(actual, hasEntry(2, "2"));
        assertThat(actual, hasEntry(3, "3"));
        assertThat(actual, hasEntry(4, "4"));
        assertThat(actual, hasEntry(5, "5"));

        assertThat(actual.entrySet(), hasSize(5));
        assertThat(actual, is(instanceOf(HashMap.class)));
    }

}