package com.robertboothby.djenni.core;

import com.robertboothby.djenni.helper.DataDistributionAssessment;
import com.robertboothby.djenni.matcher.Matchers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.robertboothby.djenni.core.ExplicitlyBiassedSupplier.biasDetail;
import static com.robertboothby.djenni.core.ExplicitlyBiassedSupplierBuilder.explicitlyBiassedSupplierFor;
import static com.robertboothby.djenni.core.SupplierHelper.fix;
import static com.robertboothby.djenni.core.util.Collections.asSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;

/**
 *
 * @author robertboothby
 */
public class ExplicitlyBiassedSupplierTest {

    @Test
    public void shouldEventuallyProduceAllValues() {
        //Given
        List<ExplicitlyBiassedSupplier.BiasDetail<TestEnum>> biasList = new ArrayList<>();
        for(TestEnum value : TestEnum.values()){
            biasList.add(biasDetail(fix(value), 1.0D));
        }

        //When
        final ExplicitlyBiassedSupplier<TestEnum> biasedSupplier = new ExplicitlyBiassedSupplier<TestEnum>(biasList);

        //Then
        assertThat(biasedSupplier, Matchers.eventuallySuppliesAllValues(asSet(TestEnum.values()), 100));
    }

    @Test
    public void shouldProducedBiasedValues() {
        //Given
        List<ExplicitlyBiassedSupplier.BiasDetail<Boolean>> biasList = new ArrayList<>();
        biasList.add(biasDetail(fix(Boolean.TRUE), 0.9D));
        biasList.add(biasDetail(fix(Boolean.FALSE), 0.1D));

        //When
        final ExplicitlyBiassedSupplier<Boolean> biasedSupplier = new ExplicitlyBiassedSupplier<Boolean>(biasList);
        final DataDistributionAssessment<Boolean> assessment = DataDistributionAssessment.assessSupplier(biasedSupplier, 1000);

        //Then
        assertThat(assessment.proportionGreaterThanOrEqualTo(Boolean.TRUE), is(both(greaterThan(0.85D)).and(lessThan(0.95D))));
        assertThat(assessment.proportionLessThanOrEqualTo(Boolean.FALSE), is(both(greaterThan(0.05D)).and(lessThan(0.15D))));
    }
}
