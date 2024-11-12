package com.robertboothby.djenni.dynamic;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.Test;

import static com.robertboothby.djenni.dynamic.DefaultSuppliersImpl.defaultObjectSupplier;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class DefaultSuppliersImplTest {

    @Test
    public void shouldSupplyDefaultPrimitiveValues() {
        DefaultSuppliers underTest = new DefaultSuppliersImpl();
        assertThat(underTest.getSupplierForClass(byte.class).get(), is((byte)0));
        assertThat(underTest.getSupplierForClass(short.class).get(), is((short)0));
        assertThat(underTest.getSupplierForClass(int.class).get(), is(0));
        assertThat(underTest.getSupplierForClass(long.class).get(), is((long)0));
        assertThat(underTest.getSupplierForClass(float.class).get(), is(0.0f));
        assertThat(underTest.getSupplierForClass(double.class).get(), is(0.0d));
        assertThat(underTest.getSupplierForClass(boolean.class).get(), is(false));
        assertThat(underTest.getSupplierForClass(char.class).get(), is('\u0000'));
    }

    @Test
    public void shouldTakeClassOverride() {
        DefaultSuppliers underTest = new DefaultSuppliersImpl();
        StreamableSupplier<String> newDefault = () -> "HELLO";

        assertThat(underTest.getSupplierForClass(String.class), is(not(newDefault)));

        underTest.setClassSupplier(String.class, newDefault);
        assertThat(underTest.getSupplierForClass(String.class), is(newDefault));
    }
    
    @Test
    public void shouldTakePropertyAndClassDefault() {
        DefaultSuppliers underTest = new DefaultSuppliersImpl();
        StreamableSupplier<String> newDefault = () -> "WORLD";

        assertThat(underTest.getSupplierForClassAndProperty(String.class, "greeting"), is(defaultObjectSupplier));

        underTest.setClassAndPropertySupplier(String.class, "greeting", newDefault);
        assertThat(underTest.getSupplierForClassAndProperty(String.class, "greeting"), is(newDefault));
        assertThat(underTest.getSupplierForClass(String.class), is(defaultObjectSupplier));
    }

    @Test public void shouldSupplyFloatPrimitiveAndThenOverride() {
        //First Check that we have the correct type.
        DefaultSuppliers defaultSuppliers = DefaultSuppliers.defaultSuppliers();
        assertThat(defaultSuppliers, is(instanceOf(DefaultSuppliersImpl.class)));

        StreamableSupplier<Float> supplierForClass = defaultSuppliers.getSupplierForClass(float.class);
        assertThat(supplierForClass.get(), is(equalTo(0.0f)));

        defaultSuppliers.setClassSupplier(float.class, () -> 1.0f);
        assertThat(defaultSuppliers.getSupplierForClass(float.class).get(), is(equalTo(1.0f)));
    }

    @Test public void shouldSupplyDoublePrimitiveAndThenOverride() {
        //First Check that we have the correct type.
        DefaultSuppliers defaultSuppliers = DefaultSuppliers.defaultSuppliers();
        assertThat(defaultSuppliers, is(instanceOf(DefaultSuppliersImpl.class)));

        StreamableSupplier<Double> supplierForClass = defaultSuppliers.getSupplierForClass(double.class);
        assertThat(supplierForClass.get(), is(equalTo(0.0d)));

        defaultSuppliers.setClassSupplier(double.class, () -> 1.0d);
        assertThat(defaultSuppliers.getSupplierForClass(double.class).get(), is(equalTo(1.0d)));
    }

    @Test public void shouldSupplyBooleanPrimitiveAndThenOverride() {
        //First Check that we have the correct type.
        DefaultSuppliers defaultSuppliers = DefaultSuppliers.defaultSuppliers();
        assertThat(defaultSuppliers, is(instanceOf(DefaultSuppliersImpl.class)));

        StreamableSupplier<Boolean> supplierForClass = defaultSuppliers.getSupplierForClass(boolean.class);
        assertThat(supplierForClass.get(), is(equalTo(false)));

        defaultSuppliers.setClassSupplier(boolean.class, () -> true);
        assertThat(defaultSuppliers.getSupplierForClass(boolean.class).get(), is(equalTo(true)));
    }

    @Test public void shouldSupplyCharPrimitiveAndThenOverride() {
        //First Check that we have the correct type.
        DefaultSuppliers defaultSuppliers = DefaultSuppliers.defaultSuppliers();
        assertThat(defaultSuppliers, is(instanceOf(DefaultSuppliersImpl.class)));

        StreamableSupplier<Character> supplierForClass = defaultSuppliers.getSupplierForClass(char.class);
        assertThat(supplierForClass.get(), is(equalTo('\u0000')));

        defaultSuppliers.setClassSupplier(char.class, () -> 'a');
        assertThat(defaultSuppliers.getSupplierForClass(char.class).get(), is(equalTo('a')));
    }

    @Test public void shouldSupplyBytePrimitiveAndThenOverride() {
        //First Check that we have the correct type.
        DefaultSuppliers defaultSuppliers = DefaultSuppliers.defaultSuppliers();
        assertThat(defaultSuppliers, is(instanceOf(DefaultSuppliersImpl.class)));

        StreamableSupplier<Byte> supplierForClass = defaultSuppliers.getSupplierForClass(byte.class);
        assertThat(supplierForClass.get(), is(equalTo((byte) 0)));

        defaultSuppliers.setClassSupplier(byte.class, () -> (byte) 1);
        assertThat(defaultSuppliers.getSupplierForClass(byte.class).get(), is(equalTo((byte) 1)));
    }


    @Test public void shouldSupplyShortPrimitiveAndThenOverride() {
        //First Check that we have the correct type.
        DefaultSuppliers defaultSuppliers = DefaultSuppliers.defaultSuppliers();
        assertThat(defaultSuppliers, is(instanceOf(DefaultSuppliersImpl.class)));

        StreamableSupplier<Short> supplierForClass = defaultSuppliers.getSupplierForClass(short.class);
        assertThat(supplierForClass.get(), is(equalTo((short) 0)));

        defaultSuppliers.setClassSupplier(short.class, () -> (short) 1);
        assertThat(defaultSuppliers.getSupplierForClass(short.class).get(), is(equalTo((short) 1)));
    }

    @Test public void shouldSupplyIntPrimitiveAndThenOverride() {
        //First Check that we have the correct type.
        DefaultSuppliers defaultSuppliers = DefaultSuppliers.defaultSuppliers();
        assertThat(defaultSuppliers, is(instanceOf(DefaultSuppliersImpl.class)));

        StreamableSupplier<Integer> supplierForClass = defaultSuppliers.getSupplierForClass(int.class);
        assertThat(supplierForClass.get(), is(equalTo(0)));

        defaultSuppliers.setClassSupplier(int.class, () -> 1);
        assertThat(defaultSuppliers.getSupplierForClass(int.class).get(), is(equalTo(1)));
    }

    @Test public void shouldSupplyLongPrimitiveAndThenOverride() {
        //First Check that we have the correct type.
        DefaultSuppliers defaultSuppliers = DefaultSuppliers.defaultSuppliers();
        assertThat(defaultSuppliers, is(instanceOf(DefaultSuppliersImpl.class)));

        StreamableSupplier<Long> supplierForClass = defaultSuppliers.getSupplierForClass(long.class);
        assertThat(supplierForClass.get(), is(equalTo(0L)));

        defaultSuppliers.setClassSupplier(long.class, () -> 1L);
        assertThat(defaultSuppliers.getSupplierForClass(long.class).get(), is(equalTo(1L)));
    }

    @Test public void shouldSupplyObjectAndThenOverride() {
        //First Check that we have the correct type.
        DefaultSuppliers defaultSuppliers = DefaultSuppliers.defaultSuppliers();
        assertThat(defaultSuppliers, is(instanceOf(DefaultSuppliersImpl.class)));

        StreamableSupplier<Object> supplierForClass = defaultSuppliers.getSupplierForClass(Object.class);
        assertThat(supplierForClass.get(), is(equalTo(null)));

        defaultSuppliers.setClassSupplier(Object.class, () -> "Hello");
        assertThat(defaultSuppliers.getSupplierForClass(Object.class).get(), is(equalTo("Hello")));
    }
}
