package com.robertboothby.djenni.time;

import java.time.Period;
import java.util.Comparator;

/**
 * Comparator for {@link Period} comparing years, months, then days.
 */
public final class PeriodComparator implements Comparator<Period> {

    public static final PeriodComparator INSTANCE = new PeriodComparator();

    private PeriodComparator() {
    }

    public static PeriodComparator periodComparator() {
        return INSTANCE;
    }

    @Override
    public int compare(Period first, Period second) {
        if (first.getYears() != second.getYears()) {
            return Integer.compare(first.getYears(), second.getYears());
        }
        if (first.getMonths() != second.getMonths()) {
            return Integer.compare(first.getMonths(), second.getMonths());
        }
        return Integer.compare(first.getDays(), second.getDays());
    }
}
