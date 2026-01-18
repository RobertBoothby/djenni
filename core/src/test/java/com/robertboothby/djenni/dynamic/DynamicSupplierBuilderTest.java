package com.robertboothby.djenni.dynamic;

import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.core.SupplierHelper;
import org.junit.Test;

import java.beans.IntrospectionException;

import static com.robertboothby.djenni.core.SupplierHelper.fix;
import static com.robertboothby.djenni.dynamic.DefaultSuppliers.defaultSuppliers;
import static com.robertboothby.djenni.dynamic.DynamicSupplierBuilder.supplierFor;
import static com.robertboothby.djenni.dynamic.DynamicSupplierBuilder.supplierForRecord;
import static com.robertboothby.djenni.lang.IntegerSupplierBuilder.anyInteger;
import static org.hamcrest.CoreMatchers.*;
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

    @Test
    public void shouldSupplyMoreThanOnce() {
        StreamableSupplier<TestClass> testClassSupplier = supplierFor(TestClass.class)
                .property(TestClass::getValueTwo, anyInteger().between(1).and(10))
                .property(TestClass::getValueOne, fix("One"))
                .property(TestClass::setValueThree, fix("Three"))
                .build();
        TestClass testClass1 = testClassSupplier.get();
        TestClass testClass2 = testClassSupplier.get();

        assertThat(testClass1.getValueOne(), is("One"));
        assertThat(testClass1.getValueTwo(), is(notNullValue()));
        assertThat(testClass1.getValueThree(), is("Three"));
        assertThat(testClass1.getValueFour(), is(0));

        assertThat(testClass2.getValueOne(), is("One"));
        assertThat(testClass2.getValueTwo(), is(notNullValue()));
        assertThat(testClass2.getValueThree(), is("Three"));
        assertThat(testClass2.getValueFour(), is(0));

    }

    @Test public void shouldClearSetterPropertyParameter() {
        DynamicSupplierBuilder<TestClass> supplierBuilder = supplierFor(TestClass.class)
                .property(TestClass::getValueTwo, anyInteger().between(1).and(10))
                .property(TestClass::getValueOne, fix("One"))
                .property(TestClass::setValueThree, fix("Three"));

        StreamableSupplier<TestClass> testClassSupplier = supplierBuilder
                .build();
        TestClass testClass = testClassSupplier.get();
        testClass.setValueThree("Four");
        testClass = testClassSupplier.get();
        assertThat(testClass.getValueThree(), is("Three"));

        supplierBuilder.clearProperty(TestClass::setValueThree);
        testClassSupplier = supplierBuilder.build();
        testClass = testClassSupplier.get();
        assertThat(testClass.getValueThree(), is(""));

    }

    @Test
    public void shouldRespectUseDefaultValueSupplier() throws IntrospectionException {
        DynamicSupplierBuilder<TestClass> supplierBuilder = supplierFor(TestClass.class);

        TestClass defaultTestClass = supplierBuilder.build().get();
        assertThat(defaultTestClass.getValueThree(), is(""));

        supplierBuilder.property(TestClass::setValueThree, SupplierHelper.nullSupplier());
        TestClass overridden = supplierBuilder.build().get();
        assertThat(overridden.getValueThree(), is(nullValue()));

        supplierBuilder.useDefaultValue(TestClass::setValueThree);
        TestClass reverted = supplierBuilder.build().get();
        assertThat(reverted.getValueThree(), is(""));
    }

    @Test
    public void builtSuppliersShouldRemainImmutableSnapshots() {
        DynamicSupplierBuilder<TestClass> builder = supplierFor(TestClass.class)
                .property(TestClass::getValueOne, fix("One"));

        StreamableSupplier<TestClass> original = builder
                .property(TestClass::setValueThree, fix("Three"))
                .build();

        // Reconfigure builder after building; the prior supplier should not change.
        builder.property(TestClass::setValueThree, fix("Four"));
        StreamableSupplier<TestClass> updated = builder.build();

        assertThat(original.get().getValueThree(), is("Three"));
        assertThat(updated.get().getValueThree(), is("Four"));
    }

    @Test
    public void shouldGenerateARecord() {
        StreamableSupplier<TestRecord> recordSupplier = supplierForRecord(TestRecord.class)
                .property("valueOne", fix("One"))
                .property("valueTwo", fix(2))
                .build();

        TestRecord record = recordSupplier.get();
        assertThat(record.valueOne(), is("One"));
        assertThat(record.valueTwo(), is(2));

        recordSupplier = supplierFor(TestRecord.class)
                .property(TestRecord::valueOne, fix("One"))
                .property(TestRecord::valueTwo, fix(2))
                .build();

        record = recordSupplier.get();
        assertThat(record.valueOne(), is("One"));
        assertThat(record.valueTwo(), is(2));
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

    public record TestRecord(String valueOne, Integer valueTwo) {
    }

}
