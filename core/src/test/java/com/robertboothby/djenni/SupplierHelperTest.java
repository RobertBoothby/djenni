package com.robertboothby.djenni;

import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.core.SupplierHelper;
import com.robertboothby.djenni.core.TestEnum;
import com.robertboothby.djenni.core.util.Collections;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.robertboothby.djenni.core.SupplierHelper.fix;
import static com.robertboothby.djenni.matcher.Matchers.eventuallySuppliesAllValues;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.Matchers.theInstance;

/**
 * Test the helper methods.
 * @author robertboothby
 */
public class SupplierHelperTest {

    @Test
    public void shouldSupplyFromValues(){
        //Given
        String[] strings = {"A", "B", "C"};

        //When
        StreamableSupplier<String> values = SupplierHelper.fromValues(strings);

        //Then
        assertThat(values, eventuallySuppliesAllValues(Collections.asSet(strings), 100));
    }

    @Test
    public void shouldSupplyFromEnum(){
        //Given
        //When
        StreamableSupplier<TestEnum> fromEnum = SupplierHelper.fromEnum(TestEnum.class);

        //Then
        assertThat(fromEnum, eventuallySuppliesAllValues(Collections.asSet(TestEnum.values()), 200));
    }

    @Test
    public void shouldDeriveFromSingleSupplier(){
        //Given
        StreamableSupplier<String> derived = SupplierHelper.derived($ -> $ + " world", fix("hello"));

        //When
        String result = derived.get();

        //Then
        assertThat(result, is("hello world"));

    }

    @Test
    public void shouldDeriveFromTwoSuppliers(){
        //Given
        StreamableSupplier<String> derived = SupplierHelper.derived((£, $) -> £ + " " + $, fix("hello"), fix("world"));

        //When
        String result = derived.get();

        //Then
        assertThat(result, is("hello world"));
    }

    @Test
    public void shouldSupplyFromRandom(){
        //Given
        //When
        StreamableSupplier<String> randomSuppliers = SupplierHelper.fromRandomSuppliers(fix("A"), fix("B"), fix("C"), fix("D"));

        //Then
        assertThat(randomSuppliers, eventuallySuppliesAllValues(Collections.asSet("A", "B", "C", "D"), 200));
    }

    @Test
    public void fixedSupplierShouldReturnExactObject() {
        String value = "Test Value";
        final Supplier<String> generator = fix(value);
        assertThat(generator.get(), is(equalTo(value)));
        assertThat(generator.get(), is(theInstance(value)));
    }

    @Test
    public void shouldSupplyFromArray(){
        //Given
        String charString = "ABCDEF";

        //When
        Supplier<Character> supplier = SupplierHelper.fromValues(charString.toCharArray());

        //Then
        assertThat(supplier,
                eventuallySuppliesAllValues(charString.chars().mapToObj(c -> (char) c).collect(toSet()), 200));
    }

    @Test
    public void shouldTurnSupplierIntoStream(){
        //Given
        Supplier<String> supplier = () -> "5";

        //When
        StreamableSupplier<String> result = SupplierHelper.asStreamable(supplier);

        //Then
        assertThat(supplier, is(not(instanceOf(StreamableSupplier.class))));
        assertThat(result, is(instanceOf(StreamableSupplier.class)));
        assertThat(result, is(not(sameInstance(supplier))));
        assertThat(result.get(), is("5"));
    }

    @Test
    public void shouldTurnStreamableSupplierIntoSelf(){
        //Given
        Supplier<String> supplier = fix("A");

        //When
        StreamableSupplier<String> result = SupplierHelper.asStreamable(supplier);

        //Then
        assertThat(result, is(sameInstance(supplier)));
    }

}
