package com.robertboothby.djenni.sugar;

import org.junit.Test;

import static com.robertboothby.djenni.sugar.EasyCompare.$;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 *
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 * @author robertboothby
 */
public class EasyCompareTest {

    @Test
    public void shouldHandleLessThan() {
        assertThat("1 is less than 2", $(1).lessThan(2), is(true));
        assertThat("2 is not less than 2", $(2).lessThan(2), is(not(true)));
        assertThat("3 is not less than 2", $(3).lessThan(2), is(not(true)));
    }

    @Test
    public void shouldHandleLessThanOrEqualTo() {
        assertThat("1 is less than or equal to 2", $(1).lessThanOrEqualTo(2), is(true));
        assertThat("2 is less than or equal to 2", $(2).lessThanOrEqualTo(2), is((true)));
        assertThat("3 is not less than or equal to 2", $(3).lessThanOrEqualTo(2), is(not(true)));
    }

    @Test
    public void shouldHandleGreaterThan() {
        assertThat("1 is not greater than 2", $(1).greaterThan(2), is(not(true)));
        assertThat("2 is not greater than 2", $(2).greaterThan(2), is(not(true)));
        assertThat("3 is greater than 2", $(3).greaterThan(2), is(true));
    }

    @Test
    public void shouldHandleGreaterThanOrEqualTo() {
        assertThat("1 is not greater than or equal to 2", $(1).greaterThanOrEqualTo(2), is(not(true)));
        assertThat("2 is greater than or equal to 2", $(2).greaterThanOrEqualTo(2), is((true)));
        assertThat("3 is greater than or equal to 2", $(3).greaterThanOrEqualTo(2), is(true));
    }
}
