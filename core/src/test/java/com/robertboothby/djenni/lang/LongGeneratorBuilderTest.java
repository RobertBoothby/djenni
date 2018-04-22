package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.Generator;
import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.helper.DataDistributionAssessment;
import org.junit.Test;

import static com.robertboothby.djenni.distribution.simple.SimpleRandomLongDistribution.RIGHT_NORMAL;
import static com.robertboothby.djenni.helper.DataDistributionAssessment.assessGenerator;
import static com.robertboothby.djenni.helper.DataDistributionAssessmentSingleValueMatcher.dataDistributionAssessmentSingleValueMatcher;
import static com.robertboothby.djenni.lang.LongGeneratorBuilder.generateALong;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 *
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
