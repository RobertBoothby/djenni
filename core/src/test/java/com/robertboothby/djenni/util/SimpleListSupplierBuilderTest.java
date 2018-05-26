package com.robertboothby.djenni.util;

import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.core.util.Repeat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

import static com.robertboothby.djenni.util.SimpleListSupplierBuilder.simpleList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SimpleListSupplierBuilderTest {

    @Mock
    private Supplier<String> testSupplier;

    @Test
    public void shouldCreateSimpleListFromSupplier(){
        //Given
        given(testSupplier.get()).willReturn("VALUE");
        StreamableSupplier<List<String>> lists = simpleList($ -> $.entries(testSupplier).withSizeBetween(5).and(5));

        //When
        List<String> strings = lists.get();

        //Then
        assertThat(strings, hasSize(5));
        HashSet<String> set = new HashSet<>(strings);
        assertThat(set, hasSize(1));
        assertThat(set, hasItem("VALUE"));
        then(testSupplier).should(times(5)).get();
    }

    @Test
    @Repeat(value = 10)
    public void shouldCreateVariableLengthListFromSupplier(){
        //Given
        given(testSupplier.get()).willReturn("VALUE");
        StreamableSupplier<List<String>> lists = simpleList($ -> $.entries(testSupplier).withSizeBetween(0).and(3));

        //When
        List<String> strings = lists.get();

        //Then
        assertThat(strings.size(), is(lessThan(4)));
        assertThat(strings.size(), is(greaterThan(-1)));
        HashSet<String> set = new HashSet<>(strings);
        if (strings.size() > 0) {
            assertThat(set, hasSize(1));
            assertThat(set, hasItem("VALUE"));
        } else {
            assertThat(set, is(empty()));
        }
        then(testSupplier).should(atLeast(0)).get();
        then(testSupplier).should(atMost(3)).get();
    }
}