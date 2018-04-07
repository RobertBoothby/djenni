package com.robertboothby.djenni.lang;

import org.djenni.Generator;
import org.djenni.distribution.Distribution;
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
public class RandomBooleanGeneratorTest {

    @Mock
    Distribution<Double, Double> distribution;

    @Test
    public void shouldUseDistributionInGeneration() {
        //Given
        RandomBooleanGenerator generator = new RandomBooleanGenerator(distribution);
        given(distribution.generate(1.0D)).willReturn(0.5D);
        //When
        boolean result = generator.generate();
        //Then
        assertThat(result, is(equalTo(false)));
        verify(distribution, times(1)).generate(1.0D);
    }
}
