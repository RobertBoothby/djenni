package com.robertboothby.djenni.core;

import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

public class ThreadLocalSupplierTest {
    @Test public void testThreadLocalSupplier() {
        // Given
        ThreadLocalSupplier<String> threadLocalSupplier = new ThreadLocalSupplier<>(() -> () -> "Test Value");

        // When
        String value = threadLocalSupplier.get();

        // Then
        assertThat(value, is("Test Value"));
    }

    @Test
    public void testThreadLocalSupplierAcrossThreads() {
        Supplier<String> supplier1 = () -> "Test Value";
        Supplier<String> supplier2 = () -> "Test Value 2";


        Deque<Supplier<String>> supplierDeque = new ArrayDeque<>(List.of(supplier1, supplier2));

        ThreadLocalSupplier<String> threadLocalSupplier = new ThreadLocalSupplier<>(supplierDeque::poll);
        ExecutorService executorService = Executors.newSingleThreadExecutor();


        assertThat(threadLocalSupplier.get(), is("Test Value"));
        assertThat(threadLocalSupplier.getSupplier(), is(sameInstance(supplier1)));

        assertThat(CompletableFuture.supplyAsync(threadLocalSupplier::get, executorService).join(), is("Test Value 2"));
        assertThat(CompletableFuture.supplyAsync(threadLocalSupplier::getSupplier, executorService).join(), is(sameInstance(supplier2)));

    }
}
