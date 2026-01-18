package com.robertboothby.djenni.distribution.fullrange;

import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.sugar.And;

import java.math.BigInteger;
import java.util.Objects;

import static java.math.BigInteger.ONE;

/**
 * Full-range equivalent of {@link com.robertboothby.djenni.distribution.simple.SimpleRandomLongDistribution}. Ranges are
 * expressed using {@link BigInteger} so callers can supply {@code Long.MAX_VALUE + 1} as the exclusive upper bound.
 * The default implementation is uniform, but alternative {@link Distribution}s can be injected for custom curves.
 */
public final class FullRangeLongDistribution {

    private static final BigInteger MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
    private static final BigInteger MAX_LONG_EXCLUSIVE = BigInteger.valueOf(Long.MAX_VALUE).add(ONE);

    private final Distribution<BigInteger, BigInteger> delegate;

    private FullRangeLongDistribution(Distribution<BigInteger, BigInteger> delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
    }

    /**
     * Uniform distribution across the configured range.
     */
    public static FullRangeLongDistribution uniform() {
        return new FullRangeLongDistribution(new UniformBigIntegerDistribution());
    }

    /**
     * Advanced entry point for plugging in custom big-integer distributions.
     */
    public static FullRangeLongDistribution using(Distribution<BigInteger, BigInteger> delegate) {
        return new FullRangeLongDistribution(delegate);
    }

    /**
     * Draw the next long between the supplied bounds.
     * @param lowerInclusive inclusive lower bound (as a {@link BigInteger})
     * @param upperExclusive exclusive upper bound (as a {@link BigInteger})
     */
    public long between(BigInteger lowerInclusive, BigInteger upperExclusive) {
        validateRange(lowerInclusive, upperExclusive);
        BigInteger span = upperExclusive.subtract(lowerInclusive);
        BigInteger offset = delegate.generate(span);
        return lowerInclusive.add(offset).longValueExact();
    }

    /**
     * Convenience overload accepting primitives for the lower bound.
     */
    public long between(long lowerInclusive, BigInteger upperExclusive) {
        return between(BigInteger.valueOf(lowerInclusive), upperExclusive);
    }

    /**
     * Fluent helper mirroring other builders.
     */
    public And<Range, BigInteger> between(long lowerInclusive) {
        BigInteger lower = BigInteger.valueOf(lowerInclusive);
        validateLower(lower);
        return upperExclusive -> new Range(lower, upperExclusive);
    }

    /**
     * Sample across the entire long domain.
     */
    public long fullRange() {
        return between(MIN_LONG, MAX_LONG_EXCLUSIVE);
    }

    private static void validateLower(BigInteger lowerInclusive) {
        if (lowerInclusive.compareTo(MIN_LONG) < 0) {
            throw new IllegalArgumentException("lower bound must be >= Long.MIN_VALUE");
        }
    }

    private static void validateRange(BigInteger lowerInclusive, BigInteger upperExclusive) {
        Objects.requireNonNull(lowerInclusive, "lowerInclusive");
        Objects.requireNonNull(upperExclusive, "upperExclusive");
        validateLower(lowerInclusive);
        if (upperExclusive.compareTo(MAX_LONG_EXCLUSIVE) > 0) {
            throw new IllegalArgumentException("upper bound must be <= Long.MAX_VALUE + 1");
        }
        if (upperExclusive.compareTo(lowerInclusive) <= 0) {
            throw new IllegalArgumentException("upper bound must be greater than lower bound");
        }
    }

    /**
     * Range wrapper returned from the fluent helper.
     */
    public final class Range {
        private final BigInteger lowerInclusive;
        private final BigInteger upperExclusive;

        private Range(BigInteger lowerInclusive, BigInteger upperExclusive) {
            validateRange(lowerInclusive, upperExclusive);
            this.lowerInclusive = lowerInclusive;
            this.upperExclusive = upperExclusive;
        }

        public long nextLong() {
            return FullRangeLongDistribution.this.between(lowerInclusive, upperExclusive);
        }

        public StreamableSupplier<Long> asSupplier() {
            return this::nextLong;
        }
    }

    /**
     * Uniform {@link Distribution} implementation for {@link BigInteger} bounds.
     */
    private static final class UniformBigIntegerDistribution implements Distribution<BigInteger, BigInteger> {
        @Override
        public BigInteger generate(BigInteger bound) {
            if (bound == null || bound.compareTo(ONE) <= 0) {
                return BigInteger.ZERO;
            }
            return BigIntegerUtils.random(bound);
        }
    }
}
