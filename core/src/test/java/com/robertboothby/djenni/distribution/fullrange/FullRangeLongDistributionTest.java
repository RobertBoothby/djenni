package com.robertboothby.djenni.distribution.fullrange;

import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.distribution.Distribution;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FullRangeLongDistributionTest {

    private static final BigInteger MAX_EXCLUSIVE = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE);

    @Test
    public void shouldGenerateFullRangeValues() {
        FullRangeLongDistribution distribution = FullRangeLongDistribution.uniform();

        long value = distribution.fullRange();

        assertThat(value, allOf(
                greaterThanOrEqualTo(Long.MIN_VALUE),
                lessThanOrEqualTo(Long.MAX_VALUE)
        ));
    }

    @Test
    public void shouldGenerateWithinExplicitRange() {
        FullRangeLongDistribution distribution = FullRangeLongDistribution.uniform();
        BigInteger lower = BigInteger.valueOf(-1_000_000_000L);
        BigInteger upper = BigInteger.valueOf(1_000_000_000L);

        long value = distribution.between(lower, upper);

        assertThat(value, allOf(
                greaterThanOrEqualTo(lower.longValue()),
                lessThan(upper.longValue())
        ));
    }

    @Test
    public void shouldSupportBetweenAndHelper() {
        FullRangeLongDistribution.Range range = FullRangeLongDistribution.uniform()
                .between(-500L)
                .and(BigInteger.ZERO);

        long value = range.nextLong();
        assertThat(value, allOf(greaterThanOrEqualTo(-500L), lessThan(0L)));

        StreamableSupplier<Long> supplier = range.asSupplier();
        long second = supplier.get();
        assertThat(second, allOf(greaterThanOrEqualTo(-500L), lessThan(0L)));
    }

    @Test
    public void shouldRejectLowerBoundsBelowLongMin() {
        assertThrows(IllegalArgumentException.class, () -> FullRangeLongDistribution.uniform()
                .between(BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE), BigInteger.valueOf(Long.MIN_VALUE)));
    }

    @Test
    public void shouldRejectUpperBoundsAboveLongMaxPlusOne() {
        assertThrows(IllegalArgumentException.class, () -> FullRangeLongDistribution.uniform()
                .between(BigInteger.valueOf(-10L), MAX_EXCLUSIVE.add(BigInteger.ONE)));
    }

    @Test
    public void shouldRejectInvalidOrdering() {
        assertThrows(IllegalArgumentException.class, () -> FullRangeLongDistribution.uniform()
                .between(BigInteger.TEN, BigInteger.ONE));
    }

    @Test
    public void shouldDelegateToCustomDistribution() {
        AtomicReference<BigInteger> lastBound = new AtomicReference<>();
        Distribution<BigInteger, BigInteger> deterministic = bound -> {
            lastBound.set(bound);
            return bound.subtract(BigInteger.ONE);
        };
        FullRangeLongDistribution distribution = FullRangeLongDistribution.using(deterministic);
        BigInteger upper = BigInteger.valueOf(10L);

        long result = distribution.between(BigInteger.ZERO, upper);

        assertThat(lastBound.get(), is(upper));
        assertThat(result, is(9L));
    }
}
