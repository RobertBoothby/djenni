package com.robertboothby.djenni.helper;


import org.djenni.sugar.And;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static org.djenni.sugar.EasyCompare.$;

/**
 *
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 * @author robertboothby
 * @param <T>
 */
public class DataDistributionAssessmentRangeMatcher<T extends Comparable<T>> extends TypeSafeDiagnosingMatcher<DataDistributionAssessment<T>> {

    private final T leastValue;
    private final T maximumValue;

    public DataDistributionAssessmentRangeMatcher(T leastValue, T maximumValue) {
        this.leastValue = leastValue;
        this.maximumValue = maximumValue;
    }

    @Override
    protected boolean matchesSafely(DataDistributionAssessment<T> assessment, Description mismatchDescription) {
        if($(leastValue).lessThanOrEqualTo(assessment.leastValue()) && $(maximumValue).greaterThan(assessment.greatestValue())){
            return true;
        }

        mismatchDescription
                .appendText("Got a distribution with values between: ")
                .appendValue(assessment.leastValue())
                .appendText(" inclusive and: ")
                .appendValue(assessment.greatestValue())
                .appendText(" inclusive");

        return false;
    }

    public void describeTo(Description description) {
        description
                .appendText("To get a distribution with values between: ")
                .appendValue(leastValue)
                .appendText(" inclusive and: ")
                .appendValue(maximumValue
                ).appendText(" exclusive");
    }

    public static <T extends Comparable<T>> And<DataDistributionAssessmentRangeMatcher<T>, T> between(final T lowValue){
        return new And<DataDistributionAssessmentRangeMatcher<T>, T>() {
            public DataDistributionAssessmentRangeMatcher<T> and(final T highValue) {
                return new DataDistributionAssessmentRangeMatcher<T>(lowValue, highValue);
            }
        };
    }
}
