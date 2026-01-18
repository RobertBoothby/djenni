package com.robertboothby.djenni.sugar;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RangeTest {

    @Test
    public void shouldRequireMinimumValue() {
        assertThrows(NullPointerException.class, () -> Range.inclusive(new Object()).between(null));
    }

    @Test
    public void shouldRequireMaximumValue() {
        assertThrows(NullPointerException.class, () -> Range.inclusive(new Object()).between(1).and(null));
    }

    @Test
    public void inclusiveRangeAllowsEqualBounds() {
        Object parent = new Object();
        Object returnedParent = Range.inclusive(parent).between(3).and(3);
        assertThat(returnedParent, is(parent));
    }

    @Test
    public void inclusiveRangeRejectsMaximumLessThanMinimum() {
        assertThrows(IllegalArgumentException.class, () -> Range.inclusive(new Object()).between(3).and(2));
    }

    @Test
    public void exclusiveRangeRejectsEqualBounds() {
        assertThrows(IllegalArgumentException.class, () -> Range.exclusive(new Object()).between(3).and(3));
    }

    @Test
    public void getMinimumRequiresConfiguration() {
        assertThrows(IllegalStateException.class, () -> Range.inclusive(new Object()).getMinimum());
    }

    @Test
    public void getMaximumRequiresConfiguration() {
        Range<Object, Integer> range = Range.inclusive(new Object());
        range.between(1);
        assertThrows(IllegalStateException.class, range::getMaximum);
    }
}
