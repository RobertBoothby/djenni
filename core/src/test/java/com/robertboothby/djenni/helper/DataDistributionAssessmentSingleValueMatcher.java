package com.robertboothby.djenni.helper;

import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;

/**
 * This matcher helps assert that a DataDistributionAssessment only has one value.
 * @author robertboothby
 * @param <T> The type, extending Comparable, of the values whose distribution is being assessed.
 */
public class DataDistributionAssessmentSingleValueMatcher<T extends Comparable<T>> extends DiagnosingMatcher<DataDistributionAssessment<T>> {

    private final T expectedValue;

    /**
     * Construct an instance of the matcher with the expected single value.
     * @param expectedValue The expected single value for the assessment.
     */
    public DataDistributionAssessmentSingleValueMatcher(T expectedValue) {
        this.expectedValue = expectedValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected boolean matches(Object item, Description mismatchDescription) {
        if(item instanceof DataDistributionAssessment){
            DataDistributionAssessment<T> assessment = (DataDistributionAssessment<T>) item;
            if(assessment.greatestValue().compareTo(assessment.leastValue()) == 0){
                if (assessment.greatestValue().compareTo(expectedValue) == 0){
                    return true;
                }
                mismatchDescription.appendText(
                        "The distribution contained only values of: ")
                        .appendValue(assessment.greatestValue());
            } else {
                mismatchDescription.appendText(
                        "The distribution had more than a single value. The greatest value was: ")
                        .appendValue(assessment.greatestValue())
                        .appendText(" and the least value was: ")
                        .appendValue(assessment.leastValue());

            }
        } else {
            mismatchDescription.appendText("Not an instance of DataDistributionAssessment.");
        }
        //Default is to return false... Saves errors.
        return false;        }

    public void describeTo(Description description) {
        description.appendText("A distribution containing only values of: ");
        description.appendValue(expectedValue);
    }

    public static <T extends Comparable<T>> DataDistributionAssessmentSingleValueMatcher<T> dataDistributionAssessmentSingleValueMatcher(T expectedValue){
        return new DataDistributionAssessmentSingleValueMatcher<T>(expectedValue);
    }
}
