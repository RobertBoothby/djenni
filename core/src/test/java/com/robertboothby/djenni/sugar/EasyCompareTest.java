package com.robertboothby.djenni.sugar;

import org.junit.jupiter.api.Test;

import static com.robertboothby.djenni.sugar.EasyCompare.eC;
import static com.robertboothby.djenni.sugar.EasyCompare.easyCompare;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 *
 * @author robertboothby
 */
public class EasyCompareTest {

    @Test
    public void shouldHandleLessThan() {
        assertThat("1 is less than 2", eC(1).lessThan(2), is(true));
        assertThat("2 is not less than 2", eC(2).lessThan(2), is(not(true)));
        assertThat("3 is not less than 2", eC(3).lessThan(2), is(not(true)));
    }

    @Test
    public void shouldHandleLessThanOrEqualTo() {
        assertThat("1 is less than or equal to 2", eC(1).lessThanOrEqualTo(2), is(true));
        assertThat("2 is less than or equal to 2", eC(2).lessThanOrEqualTo(2), is((true)));
        assertThat("3 is not less than or equal to 2", eC(3).lessThanOrEqualTo(2), is(not(true)));
    }

    @Test
    public void shouldHandleGreaterThan() {
        assertThat("1 is not greater than 2", eC(1).greaterThan(2), is(not(true)));
        assertThat("2 is not greater than 2", eC(2).greaterThan(2), is(not(true)));
        assertThat("3 is greater than 2", eC(3).greaterThan(2), is(true));
    }

    @Test
    public void shouldHandleGreaterThanOrEqualTo() {
        assertThat("1 is not greater than or equal to 2", eC(1).greaterThanOrEqualTo(2), is(not(true)));
        assertThat("2 is greater than or equal to 2", eC(2).greaterThanOrEqualTo(2), is((true)));
        assertThat("3 is greater than or equal to 2", eC(3).greaterThanOrEqualTo(2), is(true));
    }

    @Test
    public void shouldHandleBefore(){
        assertThat("1 is before 2", easyCompare(1).before(2), is(true));
        assertThat("2 is not before 2", easyCompare(2).before(2), is(not(true)));
        assertThat("3 is not before 2", easyCompare(3).before(2), is(not(true)));
    }

    @Test
    public void shouldHandleAfter() {
        assertThat("1 is not after 2", eC(1).after(2), is(not(true)));
        assertThat("2 is not after 2", eC(2).after(2), is(not(true)));
        assertThat("3 is after 2", eC(3).after(2), is(true));
    }

    @Test
    public void shouldHandleBetween() {
        assertThat("1 is between 0 and 1", eC(1).between(0).and(1), is(true));
        assertThat("2 is between 2 and 5", eC(2).between(2).and(5), is(true));
        assertThat("3 is between 2 and 6", eC(3).between(2).and(6), is(true));
        assertThat("4 is not between 10 and 12", eC(4).between(10).and(12), is(not(true)));
        assertThat("5 is not between -1 and 3", eC(5).between(-1).and(3), is(not(true)));

    }

}
