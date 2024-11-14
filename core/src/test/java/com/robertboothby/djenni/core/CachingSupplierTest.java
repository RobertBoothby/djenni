package com.robertboothby.djenni.core;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.robertboothby.djenni.core.CachingSupplier.cacheSuppliedValues;
import static com.robertboothby.djenni.lang.StringSupplierBuilder.arbitraryString;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class CachingSupplierTest {
    @Test
    public void shouldBeAbleToConfigureLastValueSupplier(){
        //Given
        CachingSupplier<String> cachingSupplier = cacheSuppliedValues(arbitraryString().build());

        String expected = cachingSupplier.get();

        //When
        String actual = cachingSupplier.get();

        //Then
        assertThat(actual, is(sameInstance(expected)));

        //When
        cachingSupplier.next();
        String newActual = cachingSupplier.get();

        //
        assertThat(newActual, is(not(sameInstance(expected))));
    }

    @Test
    public void shouldBeAbleToOperateCachingSupplierAcrossThreads() throws ExecutionException, InterruptedException {
        CachingSupplier<String> cachingSupplier = cacheSuppliedValues(arbitraryString().build());
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        String expectedOnThisThread = cachingSupplier.get();
        String expectedOnOtherThread = CompletableFuture.supplyAsync(cachingSupplier::get, executorService).get();

        //When
        String actualOnThisThread = cachingSupplier.get();
        String actualOnOtherThread = CompletableFuture.supplyAsync(cachingSupplier::get, executorService).get();

        //Then
        assertThat(expectedOnThisThread, Matchers.notNullValue());
        assertThat(expectedOnOtherThread, Matchers.notNullValue());
        assertThat(actualOnThisThread, is(sameInstance(expectedOnThisThread)));
        assertThat(actualOnOtherThread, is(sameInstance(expectedOnOtherThread)));

        //When - call next on this thread and we get on both threads
        cachingSupplier.next();
        String newActualOnThisThread = cachingSupplier.get();
        actualOnOtherThread = CompletableFuture.supplyAsync(cachingSupplier::get, executorService).get();

        //Then - the value on this thread has changed but the value on the other thread has not.
        assertThat(newActualOnThisThread, is(not(sameInstance(expectedOnThisThread))));
        assertThat(actualOnOtherThread, is(sameInstance(expectedOnOtherThread)));

        //When - call next on the other thread and we get on both threads
        CompletableFuture.runAsync(cachingSupplier::next, executorService).get();
        String newActualOnOtherThread = CompletableFuture.supplyAsync(cachingSupplier::get, executorService).get();

        //Then - the value on the other thread has changed but the value on this thread has not.
        assertThat(newActualOnOtherThread, is(not(sameInstance(expectedOnOtherThread))));
        assertThat(newActualOnThisThread, is(cachingSupplier.get()));
    }
}
