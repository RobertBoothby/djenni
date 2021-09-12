package com.robertboothby.djenni.dynamic;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.Test;

import java.beans.IntrospectionException;

import static com.robertboothby.djenni.core.SupplierHelper.fix;
import static com.robertboothby.djenni.dynamic.DynamicSupplierBuilder.supplierFor;
import static com.robertboothby.djenni.lang.IntegerSupplierBuilder.anyInteger;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class DynamicSupplierBuilderTest {
    @Test
    public void shouldGenerateAClass() throws IntrospectionException {
        //.useConstructor($ -> new TestClass($.p("One"), $.p(Integer.class)));

        StreamableSupplier<TestClass> testClassSupplier = supplierFor(TestClass.class)
                .byGet(TestClass::getValueTwo, anyInteger().between(1).and(10))
                .byGet(TestClass::getValueOne, fix("One"))
                .bySet(TestClass::setValueThree, fix("Three"))
                .build();
        TestClass testClass = testClassSupplier.get();


        assertThat(testClass.getValueOne(), is("One"));
        assertThat(testClass.getValueTwo(), is(notNullValue()));
        assertThat(testClass.getValueThree(), is("Three"));
        assertThat(testClass.getValueFour(), is(0));
//        //OR Infinite stream
//        Stream<TestClass> testClassStream = testClassSupplier.stream();
//        //OR Stream of 10
//        Stream<TestClass> testClassStreamOfTen = testClassSupplier.stream(10);

//        DynamicSupplierBuilder<TestClass> supplierBuilder = new DynamicSupplierBuilder<>(TestClass.class)
//                .byGet($ -> $::getValueTwo, integerSupplier().between(1).and(10))
//                .byGet($ -> $::getValueOne, fix("One"));

    }
    public static class TestClass {
        private final String valueOne;

        private final Integer valueTwo;
        private String valueThree = "";
        private int valueFour;


        public TestClass(String valueOne, Integer valueTwo, int valueFour) {
            this.valueOne = valueOne;
            this.valueTwo = valueTwo;

        }

        public String getValueOne() {
            return valueOne;
        }

        public Integer getValueTwo() {
            return valueTwo;
        }

        public void setValueThree(String valueThree) {
            this.valueThree = valueThree;
        }

        public String getValueThree() {
            return valueThree;
        }
        public int getValueFour() {
            return valueFour;
        }

    }

}
