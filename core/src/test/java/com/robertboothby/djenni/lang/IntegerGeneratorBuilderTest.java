package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.helper.DataDistributionAssessment;
import com.robertboothby.djenni.helper.DataDistributionAssessmentSingleValueMatcher;
import com.robertboothby.djenni.Generator;
import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.helper.DataDistributionAssessment;
import org.junit.Test;

import static com.robertboothby.djenni.distribution.simple.SimpleRandomIntegerDistribution.RIGHT_NORMAL;
import static com.robertboothby.djenni.helper.DataDistributionAssessment.assessGenerator;
import static com.robertboothby.djenni.helper.DataDistributionAssessmentSingleValueMatcher.dataDistributionAssessmentSingleValueMatcher;
import static com.robertboothby.djenni.lang.IntegerGeneratorBuilder.integerGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 *
 * @author robertboothby
 */
public class IntegerGeneratorBuilderTest {

    @Test
    public void shouldGenerateOnlySingleValueWhenConfiguredSo() {
        //Given
        Generator<Integer> onlyValues = integerGenerator().onlyValue(5).build();
        Generator<Integer> rangeValues = integerGenerator().between(5).and(6).build();

        //When
        final DataDistributionAssessment<Integer> onlyValuesAssessment = DataDistributionAssessment.assessGenerator(onlyValues, 100);
        final DataDistributionAssessment<Integer> rangeValuesAssessment = DataDistributionAssessment.assessGenerator(rangeValues, 100);

        //Then
        assertThat("onlyValues distribution incorrect", onlyValuesAssessment, DataDistributionAssessmentSingleValueMatcher.dataDistributionAssessmentSingleValueMatcher(5));
        assertThat("rangeValues distribution incorrect", rangeValuesAssessment, DataDistributionAssessmentSingleValueMatcher.dataDistributionAssessmentSingleValueMatcher(5));
    }

    @Test
    public void shouldUseConfigureDistribution() {
        //Given
        //When
        Generator<Integer> generator = integerGenerator().between(1).and(3).withDistribution(RIGHT_NORMAL).build();
        //Then
        assertThat(generator, is(instanceOf(RandomIntegerGenerator.class)));
        RandomIntegerGenerator randomIntegerGenerator = (RandomIntegerGenerator) generator;
        final Distribution<Integer, Integer> distribution = RIGHT_NORMAL;
        assertThat(randomIntegerGenerator.getDistribution(), is(distribution));

    }

}
