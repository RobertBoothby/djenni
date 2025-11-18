package com.robertboothby.djenni.distribution.fullrange;

import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;

final class BigIntegerUtils {

    private BigIntegerUtils() {}

    /**
     * Generates a uniform random {@link BigInteger} in {@code [0, bound)} using rejection sampling.
     */
    static BigInteger random(BigInteger bound) {
        int bitLength = bound.bitLength();
        BigInteger candidate;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        do {
            candidate = new BigInteger(bitLength, random);
        } while (candidate.compareTo(bound) >= 0);
        return candidate;
    }
}
