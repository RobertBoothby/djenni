package com.robertboothby.djenni.util;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.Test;

import java.util.BitSet;
import java.util.concurrent.atomic.AtomicInteger;

import static com.robertboothby.djenni.core.SupplierHelper.fix;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class BitSetSupplierBuilderTest {

    @Test
    public void shouldBuildFromBooleanSupplier() {
        AtomicInteger counter = new AtomicInteger();
        StreamableSupplier<BitSet> bitSets = BitSetSupplierBuilder.bitSetSupplier()
                .bitLengthSupplier(fix(4))
                .fromBooleans(() -> (counter.incrementAndGet() % 2) == 0)
                .build();

        BitSet expected = new BitSet(4);
        expected.set(1);
        expected.set(3);

        assertThat(bitSets.get(), is(equalTo(expected)));
    }

    @Test
    public void shouldBuildFromByteSupplier() {
        AtomicInteger counter = new AtomicInteger();
        StreamableSupplier<BitSet> bitSets = BitSetSupplierBuilder.bitSetSupplier()
                .bitLengthSupplier(fix(12))
                .fromBytes(() -> (byte) counter.incrementAndGet())
                .build();

        byte[] bytes = {(byte) 1, (byte) 2};
        BitSet expected = BitSet.valueOf(bytes);
        if (expected.length() > 12) {
            expected.clear(12, expected.length());
        }

        assertThat(bitSets.get(), is(equalTo(expected)));
    }

    @Test
    public void shouldBuildFromByteArraySupplier() {
        byte[] data = {(byte) 0x5A, (byte) 0x10};
        StreamableSupplier<BitSet> bitSets = BitSetSupplierBuilder.bitSetSupplier()
                .fromByteArrays(() -> data)
                .build();

        BitSet expected = BitSet.valueOf(data);

        assertThat(bitSets.get(), is(equalTo(expected)));
    }
}
