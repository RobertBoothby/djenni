package com.robertboothby.djenni.util;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Builds suppliers of {@link UUID}. By default {@link UUID#randomUUID()} is used for each generated value.
 */
public class UUIDSupplierBuilder implements ConfigurableSupplierBuilder<UUID, UUIDSupplierBuilder> {

    private Supplier<UUID> uuidGenerator = UUID::randomUUID;

    public static UUIDSupplierBuilder uuidSupplier() {
        return new UUIDSupplierBuilder();
    }

    @Override
    public StreamableSupplier<UUID> build() {
        Supplier<UUID> generator = this.uuidGenerator;
        return generator::get;
    }

    /**
     * Override the generator used to create UUID values.
     * @param generator supplier invoked for each UUID
     * @return this builder for further configuration
     */
    public UUIDSupplierBuilder withGenerator(Supplier<UUID> generator) {
        this.uuidGenerator = Objects.requireNonNull(generator, "generator");
        return this;
    }
}
