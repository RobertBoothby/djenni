package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.helper.DataDistributionAssessment;
import com.robertboothby.djenni.helper.DataDistributionAssessmentRangeMatcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Supplier;

import static com.robertboothby.djenni.helper.DataDistributionAssessmentSingleValueMatcher.dataDistributionAssessmentSingleValueMatcher;
import static com.robertboothby.djenni.lang.LongSupplierBuilder.generateALong;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 *
 * @author robertboothby
 */
@ExtendWith(MockitoExtension.class)
public class LongSupplierBuilderTest {
    @Mock
    Distribution<Long, Long> distribution;

    @Test
    public void shouldGenerateOnlySingleValueWhenConfiguredSo() {
        //Given
        Supplier<Long> onlyValues = generateALong().onlyValue(5L).build();
        Supplier<Long> rangeValues = generateALong().between(5L).and(6L).build();

        //When
        final DataDistributionAssessment<Long> onlyValuesAssessment = DataDistributionAssessment.assessSupplier(onlyValues, 100);
        final DataDistributionAssessment<Long> rangeValuesAssessment = DataDistributionAssessment.assessSupplier(rangeValues, 100);

        //Then
        assertThat("onlyValues distribution incorrect", onlyValuesAssessment, dataDistributionAssessmentSingleValueMatcher(5L));
        assertThat("rangeValues distribution incorrect", rangeValuesAssessment, dataDistributionAssessmentSingleValueMatcher(5L));
    }


    @Test
    public void shouldGenerateARangeOfValuesWhenConfiguredToDoSo() {
        //Given
        Supplier<Long> generator = generateALong().between(-10L).and(10L).build();

        //When
        final DataDistributionAssessment<Long> assessment = DataDistributionAssessment.assessSupplier(generator, 100);

        //Then
        assertThat(assessment, DataDistributionAssessmentRangeMatcher.between(-10L).and(10L));
    }

    @Test
    public void shouldUseDistributionInGeneration() {
        //Given
        Supplier<Long> generator = generateALong().between(-10L).and(10L).distribution(distribution).build();
        given(distribution.generate(20L)).willReturn(5L);
        //When
        long result = generator.get();
        //Then
        assertThat(result, is(equalTo(-5L)));
        verify(distribution, times(1)).generate(20L);
    }

    @Test
    public void builtLongSuppliersShouldRemainStableAfterBuilderChanges() {
        LongSupplierBuilder builder = generateALong().onlyValue(7L);

        Supplier<Long> supplier = builder.build();

        builder.onlyValue(9L);

        assertThat(supplier.get(), is(7L));
    }

}
