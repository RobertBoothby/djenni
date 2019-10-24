package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Trivial supplier builder that simply supplies a random byte. No fancy distributions or anything else.
 * TODO consider if this one could be grown into something more.
 */
public class ByteSupplierBuilder implements ConfigurableSupplierBuilder<Byte, ByteSupplierBuilder> {

    @Override
    public StreamableSupplier<Byte> build() {
        return () -> {
            byte[] single = new byte[1];
            ThreadLocalRandom.current().nextBytes(single);
            return single[0];
        };
    }

    public static ByteSupplierBuilder byteSupplier() {
        return new ByteSupplierBuilder();
    }
}
