package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.helper.DataDistributionAssessment;
import com.robertboothby.djenni.helper.DataDistributionAssessmentRangeMatcher;
import org.djenni.distribution.Distribution;
import org.djenni.Generator;
import org.djenni.helper.DataDistributionAssessment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.djenni.helper.DataDistributionAssessment.assessGenerator;
import static org.djenni.helper.DataDistributionAssessmentRangeMatcher.between;
import static org.djenni.lang.IntegerGeneratorBuilder.integerGenerator;
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
public class RandomIntegerGeneratorTest {

    @Mock
    Distribution<Integer, Integer> distribution;

    @Test
    public void shouldGenerateARangeOfValuesWhenConfiguredToDoSo() {
        //Given
        Generator<Integer> generator = integerGenerator().between(-10).and(10).build();

        //When
        final DataDistributionAssessment<Integer> assessment = DataDistributionAssessment.assessGenerator(generator, 100);

        //Then
        assertThat(generator, is(instanceOf(RandomIntegerGenerator.class)));
        assertThat(assessment, DataDistributionAssessmentRangeMatcher.between(-10).and(10));
    }

    @Test
    public void shouldUseDistributionInGeneration() {
        //Given
        RandomIntegerGenerator generator = new RandomIntegerGenerator(0, 10, distribution);
        given(distribution.generate(10)).willReturn(5);
        //When
        int result = generator.generate();
        //Then
        assertThat(result, is(equalTo(5)));
        verify(distribution, times(1)).generate(10);
    }
}
