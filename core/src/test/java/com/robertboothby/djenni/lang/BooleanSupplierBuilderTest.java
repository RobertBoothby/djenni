package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.distribution.Distribution;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.function.Supplier;

import static com.robertboothby.djenni.helper.DataDistributionAssessment.assessSupplier;
import static com.robertboothby.djenni.lang.BooleanSupplierBuilder.booleanSupplier;
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
@RunWith(MockitoJUnitRunner.class)
public class BooleanSupplierBuilderTest {

    @Mock
    Distribution<Double, Double> distribution;

    @Test
    public void shouldUseDistributionInGeneration() {
        //Given
        Supplier<Boolean> supplier = booleanSupplier()
                .withDistribution(distribution)
                .build();

        given(distribution.generate(1.0D)).willReturn(0.5D);
        //When
        boolean result = supplier.get();
        //Then
        assertThat(result, is(equalTo(false)));
        verify(distribution, times(1)).generate(1.0D);
    }
}
