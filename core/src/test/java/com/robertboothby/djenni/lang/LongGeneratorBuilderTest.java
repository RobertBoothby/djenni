package com.robertboothby.djenni.lang;

import org.djenni.Generator;
import org.djenni.distribution.Distribution;
import org.djenni.helper.DataDistributionAssessment;
import org.junit.Test;

import static org.djenni.distribution.simple.SimpleRandomLongDistribution.RIGHT_NORMAL;
import static org.djenni.helper.DataDistributionAssessment.assessGenerator;
import static org.djenni.helper.DataDistributionAssessmentSingleValueMatcher.dataDistributionAssessmentSingleValueMatcher;
import static org.djenni.lang.LongGeneratorBuilder.generateALong;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 *
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 * @author robertboothby
 */
public class LongGeneratorBuilderTest {
    @Test
    public void shouldGenerateOnlySingleValueWhenConfiguredSo() {
        //Given
        Generator<Long> onlyValues = generateALong().onlyValue(5L).build();
        Generator<Long> rangeValues = generateALong().between(5L).and(6L).build();

        //When
        final DataDistributionAssessment<Long> onlyValuesAssessment = assessGenerator(onlyValues, 100);
        final DataDistributionAssessment<Long> rangeValuesAssessment = assessGenerator(rangeValues, 100);

        //Then
        assertThat("onlyValues distribution incorrect", onlyValuesAssessment, dataDistributionAssessmentSingleValueMatcher(5L));
        assertThat("rangeValues distribution incorrect", rangeValuesAssessment, dataDistributionAssessmentSingleValueMatcher(5L));
    }

    @Test
    public void shouldUseConfigureDistribution() {
        //Given
        //When
        Generator<Long> generator = generateALong().between(1L).and(3L).distribution(RIGHT_NORMAL).build();

        //Then
        assertThat(generator, is(instanceOf(RandomLongGenerator.class)));
        final Distribution<Long, Long> distribution = RIGHT_NORMAL;
        assertThat((RandomLongGenerator) generator, is(new RandomLongGenerator(
                1L, 3L, distribution
        )));
    }
}
