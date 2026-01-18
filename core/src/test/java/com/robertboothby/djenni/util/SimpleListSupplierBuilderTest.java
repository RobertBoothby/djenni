package com.robertboothby.djenni.util;

import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

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

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class SimpleListSupplierBuilderTest {

    @Mock
    private StreamableSupplier<String> testSupplier;

    @Mock
    private SupplierBuilder<String> testSupplierBuilder;

    @Test
    public void shouldCreateSimpleListFromSupplier(){
        //Given
        given(testSupplier.get()).willReturn("VALUE");
        StreamableSupplier<List<String>> lists = simpleList($ -> $.entries(testSupplier).size(5));

        //When
        List<String> strings = lists.get();

        //Then
        assertThat(strings, hasSize(5));
        HashSet<String> set = new HashSet<>(strings);
        assertThat(set, hasSize(1));
        assertThat(set, hasItem("VALUE"));
        then(testSupplier).should(times(5)).get();
    }

    @RepeatedTest(10)
    @SuppressWarnings("unchecked")
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
        //Need to reset the mock due to the use of a repeat rule.
        Mockito.reset(testSupplier);
    }

    @Test
    public void shouldCreateEntriesSupplierFromSupplierBuilder(){
        //Given
        given(testSupplierBuilder.build()).willReturn(testSupplier);

        //When
        simpleList().entries(testSupplierBuilder);

        //Then
        then(testSupplierBuilder).should(times(1)).build();
    }

    @Test
    public void builtListSuppliersShouldRemainStableAfterBuilderChanges() {
        SimpleListSupplierBuilder<String> builder = SimpleListSupplierBuilder.<String>simpleList()
                .entries(() -> "value")
                .size(1);

        StreamableSupplier<List<String>> supplier = builder.build();

        builder.entries(() -> "other");

        assertThat(supplier.get(), hasItem("value"));
    }
}
