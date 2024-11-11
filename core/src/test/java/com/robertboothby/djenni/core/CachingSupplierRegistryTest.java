package com.robertboothby.djenni.core;

import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import static com.robertboothby.djenni.core.CachingSupplier.cacheSuppliedValues;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;

@SuppressWarnings("unchecked")
public class CachingSupplierRegistryTest {
    @Test
    public void shouldCallGetOnAdd() {
        //Given
        CachingSupplierRegistry registry = CachingSupplierRegistry.registry();
        Supplier<?> mockSupplier = mock(Supplier.class);

        //When
        registry.addCachingSupplier(cacheSuppliedValues(mockSupplier));

        //Then
        BDDMockito.then(mockSupplier).should(Mockito.times(0)).get();
    }

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
        clearInvocations(mockSupplier1, mockSupplier2, mockSupplier3);

        //When
        registry.nextAll();

        //Then - called again on nextAll
        BDDMockito.then(mockSupplier1).should(Mockito.times(1)).get();
        BDDMockito.then(mockSupplier2).should(Mockito.times(1)).get();
        BDDMockito.then(mockSupplier3).should(Mockito.times(1)).get();
    }

    @Test
    public void shouldNextDefaultGroup() {
        //Given
        CachingSupplierRegistry registry = CachingSupplierRegistry.registry();
        Supplier<?> mockSupplier1 = mock(Supplier.class);
        Supplier<?> mockSupplier2 = mock(Supplier.class);
        Supplier<?> mockSupplier3 = mock(Supplier.class);
        registry.addCachingSupplier(cacheSuppliedValues(mockSupplier1));
        registry.addCachingSupplier("test", cacheSuppliedValues(mockSupplier2));
        registry.addCachingSupplier("test2", cacheSuppliedValues(mockSupplier3));
        clearInvocations(mockSupplier1, mockSupplier2, mockSupplier3);

        //When
        registry.next();

        //Then
        BDDMockito.then(mockSupplier1).should(Mockito.times(1)).get();
        BDDMockito.then(mockSupplier2).should(Mockito.times(0)).get();
        BDDMockito.then(mockSupplier3).should(Mockito.times(0)).get();

    }

    @Test
    public void shouldNextSpecifiedGroup() {
        //Given
        CachingSupplierRegistry registry = CachingSupplierRegistry.registry();
        Supplier<?> mockSupplier1 = mock(Supplier.class);
        Supplier<?> mockSupplier2 = mock(Supplier.class);
        Supplier<?> mockSupplier3 = mock(Supplier.class);
        registry.addCachingSupplier(cacheSuppliedValues(mockSupplier1));
        registry.addCachingSupplier("test", cacheSuppliedValues(mockSupplier2));
        registry.addCachingSupplier("test2", cacheSuppliedValues(mockSupplier3));
        clearInvocations(mockSupplier1, mockSupplier2, mockSupplier3);

        //When
        registry.next("test");

        //Then
        BDDMockito.then(mockSupplier1).should(Mockito.times(0)).get();
        BDDMockito.then(mockSupplier2).should(Mockito.times(1)).get();
        BDDMockito.then(mockSupplier3).should(Mockito.times(0)).get();
    }

    @Test
    public void shouldHandleThreadLocalSupplier() {
        //Given
        CachingSupplierRegistry registry = CachingSupplierRegistry.registry();
        Supplier<Supplier<String>> mockSupplierSupplier = mock(Supplier.class);
        Supplier<String> mockSupplier1 = mock(Supplier.class);
        Supplier<String> mockSupplier2 = mock(Supplier.class);
        BDDMockito.when(mockSupplierSupplier.get()).thenReturn(mockSupplier1, mockSupplier2);
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        registry.addCachingSupplier(cacheSuppliedValues(SupplierHelper.threadLocal(mockSupplierSupplier)));

        //When
        //TODO multiple thread test with nextAll()
    }

}
