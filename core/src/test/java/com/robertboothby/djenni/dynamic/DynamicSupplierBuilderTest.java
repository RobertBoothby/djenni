package com.robertboothby.djenni.dynamic;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.Test;

import java.beans.IntrospectionException;

import static com.robertboothby.djenni.core.SupplierHelper.fix;
import static com.robertboothby.djenni.dynamic.DefaultSuppliers.defaultSuppliers;
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
                .property(TestClass::getValueTwo, anyInteger().between(1).and(10))
                .property(TestClass::getValueOne, fix("One"))
                .property(TestClass::setValueThree, fix("Three"))
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
//
//        DynamicSupplierBuilder<TestClass> supplierBuilder = supplierFor(TestClass.class)
//                .property(TestClass::getValueTwo, integerSupplier().between(1).and(10))
//                .property(TestClass::getValueOne, fix("One"));

        testClassSupplier = supplierFor(TestClass.class)
                .property(TestClass::setValueFour, anyInteger().between(1).and(2))
                .build();

        testClass = testClassSupplier.get();

        assertThat(testClass.getValueFour(), is(1));

    }

    @Test
    public void shouldGenerateClassWithDefaults() throws IntrospectionException {
        defaultSuppliers().setClassAndPropertySupplier(String.class, "valueOne", fix("One"));
        defaultSuppliers().setClassAndPropertySupplier(Integer.class, "valueTwo", fix(2));
        defaultSuppliers().setClassSupplier(String.class, fix("STRING"));
        defaultSuppliers().setClassSupplier(int.class, fix(4));
        StreamableSupplier<TestClass> testClassSupplier = supplierFor(TestClass.class).build();
        TestClass testClass = testClassSupplier.get();

        assertThat(testClass.getValueOne(), is("One"));
        assertThat(testClass.getValueTwo(), is(2));
        assertThat(testClass.getValueThree(), is("STRING"));
        assertThat(testClass.getValueFour(), is(4));

    }

    public static class TestClass {
        private final String valueOne;

        private final Integer valueTwo;
        private String valueThree = "";
        private int valueFour;


        public TestClass(String valueOne, Integer valueTwo) {
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

        public void setValueFour(int valueFour) {
            this.valueFour = valueFour;
        }

    }

}
