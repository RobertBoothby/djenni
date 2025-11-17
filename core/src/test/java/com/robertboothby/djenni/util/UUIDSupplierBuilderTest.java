package com.robertboothby.djenni.util;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class UUIDSupplierBuilderTest {

    @Test
    public void shouldGenerateRandomUUIDByDefault() {
        StreamableSupplier<UUID> uuids = UUIDSupplierBuilder.uuidSupplier().build();

        UUID value = uuids.get();

        assertThat(value, is(notNullValue()));
    }

    @Test
    public void shouldUseCustomGeneratorWhenProvided() {
        AtomicInteger counter = new AtomicInteger();
        StreamableSupplier<UUID> uuids = UUIDSupplierBuilder.uuidSupplier()
                .withGenerator(() -> new UUID(0L, counter.incrementAndGet()))
                .build();

        assertThat(uuids.get(), is(equalTo(new UUID(0L, 1))));
        assertThat(uuids.get(), is(equalTo(new UUID(0L, 2))));
    }
}
