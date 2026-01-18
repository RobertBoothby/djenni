package com.robertboothby.djenni.distribution.fullrange;

import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.distribution.Distribution;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicLong;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FullRangeIntegerDistributionTest {

    @Test
    public void shouldGenerateFullRangeValues() {
        FullRangeIntegerDistribution distribution = FullRangeIntegerDistribution.uniform();

        int value = distribution.fullRange();

        assertThat(value, allOf(
                greaterThanOrEqualTo(Integer.MIN_VALUE),
                lessThanOrEqualTo(Integer.MAX_VALUE)
        ));
    }

    @Test
    public void shouldGenerateWithinExplicitRange() {
        FullRangeIntegerDistribution distribution = FullRangeIntegerDistribution.uniform();

        int value = distribution.between(-100L, 100L);

        assertThat(value, allOf(greaterThanOrEqualTo(-100), lessThan(100)));
    }

    @Test
    public void shouldSupportBetweenAndHelper() {
        FullRangeIntegerDistribution.Range range = FullRangeIntegerDistribution.uniform()
                .between(-50L)
                .and(0L);

        int value = range.nextInt();
        assertThat(value, allOf(greaterThanOrEqualTo(-50), lessThan(0)));

        StreamableSupplier<Integer> supplier = range.asSupplier();
        int second = supplier.get();
        assertThat(second, allOf(greaterThanOrEqualTo(-50), lessThan(0)));
    }

    @Test
    public void shouldRejectLowerBoundsBelowIntegerMin() {
        assertThrows(IllegalArgumentException.class, () ->
                FullRangeIntegerDistribution.uniform().between((long) Integer.MIN_VALUE - 1, Integer.MIN_VALUE));
    }

    @Test
    public void shouldRejectUpperBoundsAboveIntegerMaxExclusive() {
        assertThrows(IllegalArgumentException.class, () ->
                FullRangeIntegerDistribution.uniform()
                        .between(Integer.MIN_VALUE, (long) Integer.MAX_VALUE + 2L));
    }

    @Test
    public void shouldRejectInvalidRangeOrdering() {
        assertThrows(IllegalArgumentException.class, () -> FullRangeIntegerDistribution.uniform().between(10L, 9L));
    }

    @Test
    public void shouldDelegateToProvidedDistribution() {
        AtomicLong lastBound = new AtomicLong();
        Distribution<Long, Long> deterministic = bound -> {
            lastBound.set(bound);
            return bound - 1; // always highest value
        };
        FullRangeIntegerDistribution distribution = FullRangeIntegerDistribution.using(deterministic);

        int result = distribution.between(-10L, 0L);

        assertThat(lastBound.get(), is(10L));
        assertThat(result, is(-1));
    }
}
