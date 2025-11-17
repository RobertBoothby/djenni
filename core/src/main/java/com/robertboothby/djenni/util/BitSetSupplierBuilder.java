package com.robertboothby.djenni.util;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.core.SupplierHelper;
import com.robertboothby.djenni.lang.BooleanSupplierBuilder;
import com.robertboothby.djenni.lang.ByteSupplierBuilder;

import java.util.BitSet;
import java.util.Objects;
import java.util.function.Supplier;

import static com.robertboothby.djenni.lang.IntegerSupplierBuilder.integerSupplier;

/**
 * Builds suppliers of {@link BitSet}. By default it generates a random length between 1 and 64 bits and uses a random
 * boolean supplier to determine individual bit values.
 */
public class BitSetSupplierBuilder implements ConfigurableSupplierBuilder<BitSet, BitSetSupplierBuilder> {

    private enum Source { BOOLEAN, BYTE, BYTE_ARRAY }

    private Supplier<Integer> bitLengthSupplier = integerSupplier(builder -> builder.between(1).and(65));
    private Supplier<Boolean> booleanSupplier = BooleanSupplierBuilder.booleanSupplier().build();
    private Supplier<Byte> byteSupplier = ByteSupplierBuilder.byteSupplier().build();
    private Supplier<byte[]> byteArraySupplier;
    private Source source = Source.BOOLEAN;

    public static BitSetSupplierBuilder bitSetSupplier() {
        return new BitSetSupplierBuilder();
    }

    @Override
    public StreamableSupplier<BitSet> build() {
        Supplier<Integer> bitLengths = this.bitLengthSupplier;
        Supplier<Boolean> booleans = this.booleanSupplier;
        Supplier<Byte> bytes = this.byteSupplier;
        Supplier<byte[]> byteArrays = this.byteArraySupplier;
        Source mode = this.source;

        return () -> {
            switch (mode) {
                case BYTE_ARRAY:
                    return BitSet.valueOf(byteArrays.get());
                case BYTE:
                    return fromBytes(bytes, bitLengths.get());
                case BOOLEAN:
                default:
                    return fromBooleans(booleans, bitLengths.get());
            }
        };
    }

    private BitSet fromBooleans(Supplier<Boolean> supplier, int bitLength) {
        int length = Math.max(0, bitLength);
        BitSet bitSet = new BitSet(length);
        for (int i = 0; i < length; i++) {
            if (Boolean.TRUE.equals(supplier.get())) {
                bitSet.set(i);
            }
        }
        return bitSet;
    }

    private BitSet fromBytes(Supplier<Byte> supplier, int bitLength) {
        int length = Math.max(0, bitLength);
        if (length == 0) {
            return new BitSet();
        }
        int bytesNeeded = (length + 7) / 8;
        byte[] values = new byte[bytesNeeded];
        for (int i = 0; i < bytesNeeded; i++) {
            values[i] = supplier.get();
        }
        BitSet bitSet = BitSet.valueOf(values);
        if (bitSet.length() > length) {
            bitSet.clear(length, bitSet.length());
        }
        return bitSet;
    }

    public BitSetSupplierBuilder bitLengthSupplier(Supplier<Integer> supplier) {
        this.bitLengthSupplier = SupplierHelper.asStreamable(Objects.requireNonNull(supplier, "supplier"));
        return this;
    }

    public BitSetSupplierBuilder bitLengthSupplier(SupplierBuilder<Integer> builder) {
        Objects.requireNonNull(builder, "builder");
        return bitLengthSupplier(builder.build());
    }

    public BitSetSupplierBuilder fromBooleans(Supplier<Boolean> supplier) {
        this.booleanSupplier = SupplierHelper.asStreamable(Objects.requireNonNull(supplier, "supplier"));
        this.source = Source.BOOLEAN;
        return this;
    }

    public BitSetSupplierBuilder fromBooleans(SupplierBuilder<Boolean> builder) {
        Objects.requireNonNull(builder, "builder");
        return fromBooleans(builder.build());
    }

    public BitSetSupplierBuilder fromBytes(Supplier<Byte> supplier) {
        this.byteSupplier = SupplierHelper.asStreamable(Objects.requireNonNull(supplier, "supplier"));
        this.source = Source.BYTE;
        return this;
    }

    public BitSetSupplierBuilder fromBytes(SupplierBuilder<Byte> builder) {
        Objects.requireNonNull(builder, "builder");
        return fromBytes(builder.build());
    }

    public BitSetSupplierBuilder fromByteArrays(Supplier<byte[]> supplier) {
        this.byteArraySupplier = Objects.requireNonNull(supplier, "supplier");
        this.source = Source.BYTE_ARRAY;
        return this;
    }

    public BitSetSupplierBuilder fromByteArrays(SupplierBuilder<byte[]> builder) {
        Objects.requireNonNull(builder, "builder");
        return fromByteArrays(builder.build());
    }
}
