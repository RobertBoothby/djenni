package com.robertboothby.djenni.core;

import com.robertboothby.djenni.lang.IntegerSupplierBuilder;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.util.function.Supplier;

import static com.robertboothby.djenni.core.CachingSupplier.cacheSuppliedValues;
import static org.mockito.Mockito.mock;

public class CachingSupplierRegistryTest {
    @Test
    public void shouldNextAll() {
        //Given
        CachingSupplierRegistry registry = CachingSupplierRegistry.registry();
        Supplier<?> mockSupplier1 = mock(Supplier.class);
        Supplier<?> mockSupplier2 = mock(Supplier.class);
        Supplier<?> mockSupplier3 = mock(Supplier.class);
        registry.addCachingSupplier(cacheSuppliedValues(mockSupplier1));
        registry.addCachingSupplier("test", cacheSuppliedValues(mockSupplier2));
        registry.addCachingSupplier("test2", cacheSuppliedValues(mockSupplier3));


        //When
        registry.nextAll();

        //Then

    }

    @Test
    public void shouldNextDefaultGroup() {

    }

    @Test
    public void shouldNextSpecifiedGroup() {

    }

    @Test
    public void shouldHandleThreadLocalSupplier() {

    }

}
