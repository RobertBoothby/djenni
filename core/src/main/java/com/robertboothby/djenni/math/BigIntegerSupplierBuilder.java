package com.robertboothby.djenni.math;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.lang.ByteSupplierBuilder;

import java.math.BigInteger;
import java.util.LongSummaryStatistics;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.robertboothby.djenni.core.SupplierHelper.derived;

public class BigIntegerSupplierBuilder implements ConfigurableSupplierBuilder<BigInteger, BigIntegerSupplierBuilder> {

    private BigInteger minimumInclusiveValue = BigInteger.ZERO;
    private BigInteger maximumExclusiveValue = BigInteger.TEN;

    @Override
    public StreamableSupplier<BigInteger> build() {
        BigInteger range = maximumExclusiveValue.subtract(minimumInclusiveValue);
        int bits = range.bitLength();
        int wholeBytes = bits / 8;
        int remainderBits = bits % 8;
        int bytesToGenerate = wholeBytes + (remainderBits  == 0 ? 0 : 1);

        byte mostSignificantByteMask = remainderBits == 0 ? (byte) 0 : (byte)(-1 >>> (32 - remainderBits));

        StreamableSupplier<Byte> byteSupplier = ByteSupplierBuilder.byteSupplier().build();
        final BigInteger actualMinimum = minimumInclusiveValue;
        return () -> {
            BigInteger offsetFromMinimum = range;
            while(offsetFromMinimum.compareTo(range) >= 0) {
                byte[] valueBytes = new byte[bytesToGenerate];
                for (int i = 0; i < bytesToGenerate; i++) {
                    valueBytes[i] = byteSupplier.get();
                }
                valueBytes[0] &= mostSignificantByteMask;
                offsetFromMinimum = new BigInteger(valueBytes);
            }
            return actualMinimum.add(offsetFromMinimum);
        };
    }

    public BigIntegerSupplierBuilder minimumInclusiveValue(BigInteger minimumInclusiveValue) {
        this.minimumInclusiveValue = minimumInclusiveValue;
        return this;
    }

    public BigIntegerSupplierBuilder maximumExclusiveValue(BigInteger maximumExclusiveValue) {
        this.maximumExclusiveValue = maximumExclusiveValue;
        return this;
    }

    public static BigIntegerSupplierBuilder bigIntegers(){
        return new BigIntegerSupplierBuilder();
    }

    public static StreamableSupplier<BigInteger> bigIntegers(Supplier<Long> longSupplier){
        return derived(BigInteger::valueOf, longSupplier);
    }
}
