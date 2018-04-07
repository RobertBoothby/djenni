package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.helper.DataDistributionAssessment;
import com.robertboothby.djenni.helper.DataDistributionAssessmentRangeMatcher;
import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.Generator;
import com.robertboothby.djenni.helper.DataDistributionAssessment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.robertboothby.djenni.helper.DataDistributionAssessment.assessGenerator;
import static com.robertboothby.djenni.helper.DataDistributionAssessmentRangeMatcher.between;
import static com.robertboothby.djenni.lang.LongGeneratorBuilder.generateALong;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 *
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 * @author robertboothby
 */
@RunWith(MockitoJUnitRunner.class)
public class RandomLongGeneratorTest {

    @Mock
    Distribution<Long, Long> distribution;

    @Test
    public void shouldGenerateARangeOfValuesWhenConfiguredToDoSo() {
        //Given
        Generator<Long> generator = generateALong().between(-10L).and(10L).build();

        //When
        final DataDistributionAssessment<Long> assessment = DataDistributionAssessment.assessGenerator(generator, 100);

        //Then
        assertThat(generator, is(instanceOf(RandomLongGenerator.class)));
        assertThat(assessment, DataDistributionAssessmentRangeMatcher.between(-10L).and(10L));
    }

    @Test
    public void shouldUseDistributionInGeneration() {
        //Given
        RandomLongGenerator generator = new RandomLongGenerator(0, 10, distribution);
        given(distribution.generate(10L)).willReturn(5L);
        //When
        long result = generator.generate();
        //Then
        assertThat(result, is(equalTo(5L)));
        verify(distribution, times(1)).generate(10L);
    }
}
