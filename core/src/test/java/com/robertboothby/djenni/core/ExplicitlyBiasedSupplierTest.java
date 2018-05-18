package com.robertboothby.djenni.core;

import com.robertboothby.djenni.helper.DataDistributionAssessment;
import com.robertboothby.djenni.matcher.Matchers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.robertboothby.djenni.core.util.Collections.asSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

/**
 *
 * @author robertboothby
 */
public class ExplicitlyBiasedSupplierTest {

    private enum TestEnum {
        PANTS, SOCKS, VEST, T_SHIRT, SHOES, TROUSERS
    }

    @Test
    public void shouldEventuallyProduceAllValues() {
        //Given
        List<ExplicitlyBiasedSupplier.BiasDetail<TestEnum>> biasList = new ArrayList<>();
        for(TestEnum value : TestEnum.values()){
            biasList.add(ExplicitlyBiasedSupplier.biasDetail(value, 1.0D));
        }

        //When
        final ExplicitlyBiasedSupplier<TestEnum> biasedGenerator = new ExplicitlyBiasedSupplier<TestEnum>(biasList);

        //Then
        assertThat(biasedGenerator, Matchers.eventuallySuppliesAllValues(asSet(TestEnum.values()), 100));
    }

    @Test
    public void shouldProducedBiasedValues() {
        //Given
        List<ExplicitlyBiasedSupplier.BiasDetail<Boolean>> biasList = new ArrayList<>();
        biasList.add(ExplicitlyBiasedSupplier.biasDetail(Boolean.TRUE, 0.9D));
        biasList.add(ExplicitlyBiasedSupplier.biasDetail(Boolean.FALSE, 0.1D));

        //When
        final ExplicitlyBiasedSupplier<Boolean> biasedGenerator = new ExplicitlyBiasedSupplier<Boolean>(biasList);
        final DataDistributionAssessment<Boolean> assessment = DataDistributionAssessment.assessSupplier(biasedGenerator, 1000);

        //Then
        assertThat(assessment.proportionGreaterThanOrEqualTo(Boolean.TRUE), is(both(greaterThan(0.85D)).and(lessThan(0.95D))));
        assertThat(assessment.proportionLessThanOrEqualTo(Boolean.FALSE), is(both(greaterThan(0.05D)).and(lessThan(0.15D))));
    }
}
