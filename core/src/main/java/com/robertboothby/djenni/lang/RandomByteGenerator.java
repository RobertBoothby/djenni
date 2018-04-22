package com.robertboothby.djenni.lang;

import org.apache.commons.lang3.NotImplementedException;
import com.robertboothby.djenni.Generator;
import org.hamcrest.Description;

import java.util.concurrent.ThreadLocalRandom;


//TODO implement.

/**
 * This is a tricky one... I think that we might need a raw byte generator as well as the kind of restricted generator
 * that we've seen so far (int, longs, etc.). Maybe we need raw generators full stop for all of them to generate the
 * range... The other option is to consider whether we can derive the full range generation from a 'next up' approach.
 * A Short Generator can generate the full range and then the results can be cast down... The same approach can be
 * considered for full range shorts (integer), integers (long) and longs (BigInteger).
 */
public class RandomByteGenerator implements Generator<Byte> {
    private int minimumInclusiveValue;
    private int maximumExclusiveValue;

    public Byte generate() {
        throw new NotImplementedException("Needs implementation.");
    }

    public void describeTo(Description description) {
        throw new NotImplementedException("Needs implementation.");
    }

    public static void main(String[] args) {

        for (int i = 0; i < 2000; i++){
            int fullRange = ThreadLocalRandom.current().nextInt(256) - 128;
            byte result = (byte) fullRange;
            System.out.print(result + ", ");
        }

    }
}
