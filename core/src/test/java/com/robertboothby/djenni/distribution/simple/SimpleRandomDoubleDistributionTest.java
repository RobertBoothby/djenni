package com.robertboothby.djenni.distribution.simple;

import com.robertboothby.djenni.helper.DataDistributionAssessment;
import org.junit.jupiter.api.Test;

import static com.robertboothby.djenni.helper.DataDistributionAssessment.assessDistribution;
import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 * @author robertboothby
 */
public class SimpleRandomDoubleDistributionTest {

    @Test
    public void shouldDistributeEvenlyForUniformDistribution() {
        //Given
        //When
        final DataDistributionAssessment<Double> dataDistributionAssessment = assessDistribution(SimpleRandomDoubleDistribution.UNIFORM, 1.0D);
        //Then
        assertThat(dataDistributionAssessment.proportionLessThanOrEqualTo(0.25D), is(both(greaterThan(0.20D)).and(lessThan(0.30D))));
        assertThat(dataDistributionAssessment.proportionBetween(0.25D, 0.75D), is(both(greaterThan(0.40D)).and(lessThan(0.60D))));
        assertThat(dataDistributionAssessment.proportionGreaterThanOrEqualTo(0.75D), is(both(greaterThan(0.20D)).and(lessThan(0.30D))));
    }

    @Test
    public void shouldDistributeForNormalDistribution() {
        //Given
        //When
        final DataDistributionAssessment<Double> dataDistributionAssessment = assessDistribution(SimpleRandomDoubleDistribution.NORMAL, 1.0D);
        //Then
        //Check for bulge in the mid-range
        assertThat(dataDistributionAssessment.proportionBetween(0.15D, 0.85D), is(greaterThanOrEqualTo(0.9D)));
        assertThat(dataDistributionAssessment.proportionBetween(0.0D, 0.15D), is(lessThanOrEqualTo(0.05D)));
        assertThat(dataDistributionAssessment.proportionBetween(0.85D, 1.0D), is(lessThanOrEqualTo(0.05D)));
    }

    @Test
    public void shouldDistributeForRightNormalDistribution() {
        //Given
        //When
        final DataDistributionAssessment<Double> dataDistributionAssessment = assessDistribution(SimpleRandomDoubleDistribution.RIGHT_NORMAL, 1.0D);
        //Then
        assertThat(dataDistributionAssessment.proportionBetween(0.0D, 0.7D), is(greaterThanOrEqualTo(0.9D)));
    }

    @Test
    public void shouldDistributeForLeftNormalDistribution() {
        //Given
        //When
        final DataDistributionAssessment<Double> dataDistributionAssessment = assessDistribution(SimpleRandomDoubleDistribution.LEFT_NORMAL, 1.0D);
        //Then
        assertThat(dataDistributionAssessment.proportionBetween(0.3D, 1.0D), is(greaterThanOrEqualTo(0.9D)));

    }

    @Test
    public void shouldDistributeForInvertedNormalDistribution() {
        //Given
        //When
        final DataDistributionAssessment<Double> dataDistributionAssessment = assessDistribution(SimpleRandomDoubleDistribution.INVERTED_NORMAL, 1.0D);
        //Then
        assertThat(dataDistributionAssessment.proportionBetween(0.3D, 0.7D), is(lessThanOrEqualTo(0.1D)));
        assertThat(dataDistributionAssessment.proportionBetween(0.0D, 0.3D), is(greaterThanOrEqualTo(0.4D)));
        assertThat(dataDistributionAssessment.proportionBetween(0.7D, 1.0D), is(greaterThanOrEqualTo(0.4D)));
    }

    @Test
    public void shouldDistributeForRightInvertedNormalDistribution() {
        //Given
        //When
        final DataDistributionAssessment<Double> dataDistributionAssessment = assessDistribution(SimpleRandomDoubleDistribution.RIGHT_INVERTED_NORMAL, 1.0D);
        //Then
        assertThat(dataDistributionAssessment.proportionBetween(0.0D, 0.5D), is(lessThanOrEqualTo(0.1D)));
        assertThat(dataDistributionAssessment.proportionBetween(0.5D, 1.0D), is(greaterThanOrEqualTo(0.9D)));

    }

    @Test
    public void shouldDistributeForLeftInvertedNormalDistribution() {
        //Given
        //When
        final DataDistributionAssessment<Double> dataDistributionAssessment = assessDistribution(SimpleRandomDoubleDistribution.LEFT_INVERTED_NORMAL, 1.0D);
        //Then
        assertThat(dataDistributionAssessment.proportionBetween(0.0D, 0.5D), is(greaterThanOrEqualTo(0.9D)));
        assertThat(dataDistributionAssessment.proportionBetween(0.5D, 1.0D), is(lessThanOrEqualTo(0.1D)));
    }

}
