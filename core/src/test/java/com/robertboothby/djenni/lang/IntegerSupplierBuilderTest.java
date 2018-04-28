package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.helper.DataDistributionAssessment;
import com.robertboothby.djenni.helper.DataDistributionAssessmentRangeMatcher;
import com.robertboothby.djenni.helper.DataDistributionAssessmentSingleValueMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.function.Supplier;

import static com.robertboothby.djenni.helper.DataDistributionAssessment.assessSupplier;
import static com.robertboothby.djenni.lang.IntegerSupplierBuilder.integerSupplier;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 *
 * @author robertboothby
 */
@RunWith(MockitoJUnitRunner.class)
public class IntegerSupplierBuilderTest {

    @Mock
    Distribution<Integer, Integer> distribution;

    @Test
    public void shouldGenerateOnlySingleValueWhenConfiguredSo() {
        //Given
        Supplier<Integer> onlyValues = integerSupplier().onlyValue(5).build();
        Supplier<Integer> rangeValues = integerSupplier().between(5).and(6).build();

        //When
        final DataDistributionAssessment<Integer> onlyValuesAssessment = DataDistributionAssessment.assessSupplier(onlyValues, 100);
        final DataDistributionAssessment<Integer> rangeValuesAssessment = DataDistributionAssessment.assessSupplier(rangeValues, 100);

        //Then
        assertThat("onlyValues distribution incorrect", onlyValuesAssessment, DataDistributionAssessmentSingleValueMatcher.dataDistributionAssessmentSingleValueMatcher(5));
        assertThat("rangeValues distribution incorrect", rangeValuesAssessment, DataDistributionAssessmentSingleValueMatcher.dataDistributionAssessmentSingleValueMatcher(5));
    }


    @Test
    public void shouldGenerateARangeOfValuesWhenConfiguredToDoSo() {
        //Given
        Supplier<Integer> generator = integerSupplier().between(-10).and(10).build();

        //When
        final DataDistributionAssessment<Integer> assessment = DataDistributionAssessment.assessSupplier(generator, 100);

        //Then
        assertThat(assessment, DataDistributionAssessmentRangeMatcher.between(-10).and(10));
    }

    @Test
    public void shouldUseDistributionInGeneration() {
        //Given
        Supplier<Integer> generator = integerSupplier().between(0).and(10).withDistribution(distribution).build();

        given(distribution.generate(10)).willReturn(5);
        //When
        int result = generator.get();
        //Then
        assertThat(result, is(equalTo(5)));
        verify(distribution, times(1)).generate(10);
    }

}
