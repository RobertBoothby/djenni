package com.robertboothby.djenni.dynamic;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class DefaultSuppliersTest {

    @Test
    public void shouldSupplyDefaultInstanceAndThenOverride() {
        //First Check that we have the correct type.
        DefaultSuppliers defaultSuppliers = DefaultSuppliers.defaultSuppliers();
        assertThat(defaultSuppliers, is(instanceOf(DefaultSuppliersImpl.class)));

        //Next set up a new instance and confirm its difference to the original one.
        DefaultSuppliers newDefaultSuppliers = new DefaultSuppliersImpl();
        assertThat(newDefaultSuppliers, CoreMatchers.is(not(equalTo(defaultSuppliers))));

        //Finally, override the original default and confirm that it worked.
        DefaultSuppliers.overrideInstance(newDefaultSuppliers);
        assertThat(DefaultSuppliers.defaultSuppliers(), is(equalTo(newDefaultSuppliers)));
    }

}
