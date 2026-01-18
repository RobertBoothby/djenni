package com.robertboothby.djenni.sugar;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RangeTest {

    @Test(expected = NullPointerException.class)
    public void shouldRequireMinimumValue() {
        Range.inclusive(new Object()).between(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldRequireMaximumValue() {
        Range.inclusive(new Object()).between(1).and(null);
    }

    @Test
    public void inclusiveRangeAllowsEqualBounds() {
        Object parent = new Object();
        Object returnedParent = Range.inclusive(parent).between(3).and(3);
        assertThat(returnedParent, is(parent));
    }

    @Test(expected = IllegalArgumentException.class)
    public void inclusiveRangeRejectsMaximumLessThanMinimum() {
        Range.inclusive(new Object()).between(3).and(2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void exclusiveRangeRejectsEqualBounds() {
        Range.exclusive(new Object()).between(3).and(3);
    }

    @Test(expected = IllegalStateException.class)
    public void getMinimumRequiresConfiguration() {
        Range.inclusive(new Object()).getMinimum();
    }

    @Test(expected = IllegalStateException.class)
    public void getMaximumRequiresConfiguration() {
        Range<Object, Integer> range = Range.inclusive(new Object());
        range.between(1);
        range.getMaximum();
    }
}
