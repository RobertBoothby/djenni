package com.robertboothby.djenni.time;

import org.junit.jupiter.api.Test;

import java.time.Period;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PeriodComparatorTest {

    @Test
    public void shouldComparePeriods() {
        assertThat(PeriodComparator.periodComparator().compare(Period.of(1, 0, 0), Period.of(0, 11, 0)) > 0, is(true));
    }
}
