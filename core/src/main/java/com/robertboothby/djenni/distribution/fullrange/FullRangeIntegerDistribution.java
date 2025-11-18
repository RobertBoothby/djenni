package com.robertboothby.djenni.distribution.fullrange;

import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.distribution.simple.SimpleRandomLongDistribution;
import com.robertboothby.djenni.sugar.And;

import java.util.Objects;

/**
 * Generates {@link Integer} values across arbitrary ranges, including the entire {@code int} domain. It does so by
 * delegating to a {@link Distribution}&lt;{@link Long}, {@link Long}&gt; that produces unsigned offsets which are then
 * shifted into the caller's requested window. Ranges are expressed with {@code long} bounds to allow the sentinel
 * value {@code Integer.MAX_VALUE + 1} for exclusive upper bounds.
 */
public final class FullRangeIntegerDistribution {

    private static final long MIN_INT_AS_LONG = Integer.MIN_VALUE;
    private static final long MAX_INT_EXCLUSIVE_AS_LONG = (long) Integer.MAX_VALUE + 1L;

    private final Distribution<Long, Long> delegate;

    private FullRangeIntegerDistribution(Distribution<Long, Long> delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
    }

    /**
     * Creates a distribution that approximates a uniform curve across the configured range.
     */
    public static FullRangeIntegerDistribution uniform() {
        return new FullRangeIntegerDistribution(SimpleRandomLongDistribution.UNIFORM);
    }

    /**
     * Creates a distribution that approximates a centred normal curve across the configured range.
     */
    public static FullRangeIntegerDistribution normal() {
        return new FullRangeIntegerDistribution(SimpleRandomLongDistribution.NORMAL);
    }

    /**
     * Advanced factory for injecting a custom long-based distribution (primarily useful for testing).
     */
    public static FullRangeIntegerDistribution using(Distribution<Long, Long> delegate) {
        return new FullRangeIntegerDistribution(delegate);
    }

    /**
     * Draw the next integer in the specified interval.
     * @param lowerInclusive lower bound (inclusive) expressed as a {@code long}. Must be &ge; {@link Integer#MIN_VALUE}.
     * @param upperExclusive upper bound (exclusive) expressed as a {@code long}. Must be &le;
     *                       {@code Integer.MAX_VALUE + 1} to allow full-range coverage.
     * @return a value {@code v} such that {@code lowerInclusive ≤ v < upperExclusive}
     */
    public int between(long lowerInclusive, long upperExclusive) {
        validateRange(lowerInclusive, upperExclusive);
        long span = upperExclusive - lowerInclusive;
        long offset = delegate.generate(span);
        return (int) (lowerInclusive + offset);
    }

    /**
     * Fluent helper mirroring the&nbsp;“between… and…” pattern used throughout the builders. Example:
     * <pre>
     *     int any = FullRangeIntegerDistribution.uniform()
     *                     .between(Integer.MIN_VALUE)
     *                     .and((long) Integer.MAX_VALUE + 1)
     *                     .nextInt();
     * </pre>
     * @param lowerInclusive lower bound (inclusive)
     * @return an {@link And} helper whose {@code and()} call finalises the range
     */
    public And<Range, Long> between(long lowerInclusive) {
        validateLower(lowerInclusive);
        return upperExclusive -> new Range(lowerInclusive, upperExclusive);
    }

    /**
     * Convenience method that samples across the entire {@code int} domain.
     */
    public int fullRange() {
        return between(MIN_INT_AS_LONG, MAX_INT_EXCLUSIVE_AS_LONG);
    }

    private static void validateLower(long lowerInclusive) {
        if (lowerInclusive < MIN_INT_AS_LONG) {
            throw new IllegalArgumentException("lower bound must be >= Integer.MIN_VALUE");
        }
    }

    private static void validateRange(long lowerInclusive, long upperExclusive) {
        validateLower(lowerInclusive);
        if (upperExclusive > MAX_INT_EXCLUSIVE_AS_LONG) {
            throw new IllegalArgumentException("upper bound must be <= Integer.MAX_VALUE + 1");
        }
        if (upperExclusive <= lowerInclusive) {
            throw new IllegalArgumentException("upper bound must be greater than lower bound");
        }
    }

    /**
     * Represents an immutable, preconfigured range that can generate values repeatedly.
     */
    public final class Range {
        private final long lowerInclusive;
        private final long upperExclusive;

        private Range(long lowerInclusive, long upperExclusive) {
            validateRange(lowerInclusive, upperExclusive);
            this.lowerInclusive = lowerInclusive;
            this.upperExclusive = upperExclusive;
        }

        /**
         * Generate the next integer within the captured range.
         */
        public int nextInt() {
            return FullRangeIntegerDistribution.this.between(lowerInclusive, upperExclusive);
        }

        /**
         * Convert this range into a {@link StreamableSupplier} for repeated sampling.
         */
        public StreamableSupplier<Integer> asSupplier() {
            return this::nextInt;
        }
    }
}
