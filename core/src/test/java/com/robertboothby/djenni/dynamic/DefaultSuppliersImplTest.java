package com.robertboothby.djenni.dynamic;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.function.Supplier;

import static com.robertboothby.djenni.dynamic.DefaultSuppliersImpl.defaultObjectSupplier;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
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
}
