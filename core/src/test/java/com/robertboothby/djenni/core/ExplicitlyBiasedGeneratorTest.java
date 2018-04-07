package com.robertboothby.djenni.core;

import com.robertboothby.djenni.helper.DataDistributionAssessment;
import com.robertboothby.djenni.matcher.Matchers;
import com.robertboothby.djenni.helper.DataDistributionAssessment;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.robertboothby.djenni.core.util.Collections.asSet;
import static com.robertboothby.djenni.helper.DataDistributionAssessment.assessGenerator;
import static com.robertboothby.djenni.matcher.Matchers.eventuallyGeneratesAllValues;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

/**
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 *
 * @author robertboothby
 */
public class ExplicitlyBiasedGeneratorTest {

    private enum TestEnum {
        PANTS, SOCKS, VEST, T_SHIRT, SHOES, TROUSERS
    }

    @Test
    public void shouldEventuallyProduceAllValues() {
        //Given
        List<ExplicitlyBiasedGenerator.BiasDetail<TestEnum>> biasList = new ArrayList<>();
        for(TestEnum value : TestEnum.values()){
            biasList.add(ExplicitlyBiasedGenerator.biasDetail(value, 1.0D));
        }

        //When
        final ExplicitlyBiasedGenerator<TestEnum> biasedGenerator = new ExplicitlyBiasedGenerator<TestEnum>(biasList);

        //Then
        assertThat(biasedGenerator, Matchers.eventuallyGeneratesAllValues(asSet(TestEnum.values()), 100));
    }

    @Test
    public void shouldProducedBiasedValues() {
        //Given
        List<ExplicitlyBiasedGenerator.BiasDetail<Boolean>> biasList = new ArrayList<>();
        biasList.add(ExplicitlyBiasedGenerator.biasDetail(Boolean.TRUE, 0.9D));
        biasList.add(ExplicitlyBiasedGenerator.biasDetail(Boolean.FALSE, 0.1D));

        //When
        final ExplicitlyBiasedGenerator<Boolean> biasedGenerator = new ExplicitlyBiasedGenerator<Boolean>(biasList);
        final DataDistributionAssessment<Boolean> assessment = DataDistributionAssessment.assessGenerator(biasedGenerator, 1000);

        //Then
        assertThat(assessment.proportionGreaterThanOrEqualTo(Boolean.TRUE), is(both(greaterThan(0.85D)).and(lessThan(0.95D))));
        assertThat(assessment.proportionLessThanOrEqualTo(Boolean.FALSE), is(both(greaterThan(0.05D)).and(lessThan(0.15D))));
    }
}
