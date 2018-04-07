package com.robertboothby.djenni.distribution.uniform;

import com.robertboothby.djenni.matcher.Matchers;
import org.djenni.distribution.Distribution;
import org.djenni.helper.DataDistributionAssessment;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.djenni.helper.DataDistributionAssessment.assessDistribution;
import static org.djenni.matcher.Matchers.eventuallyGeneratesAllValues;
import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

/**
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 *
 * @author robertboothby
 */
public class UniformByteDistributionTest {

    private static final Set<Byte> allByteValues = new HashSet<Byte>();
    static {
        for(int i = -128; i < 128; i++) {
            allByteValues.add((byte)i);
        }
    }

    @Test
    public void shouldEventuallyGenerateFullRangeOfBytes() {
        //Given
        Distribution<Byte, Integer> byteDistribution = new UniformByteDistribution();

        //When
        //Then
        MatcherAssert.assertThat(byteDistribution, Matchers.eventuallyGeneratesAllValues(allByteValues, 256, 2000));

    }

    @Test
    public void shouldDistributeEvenlyForUniformDistribution() {
        //Given
        Distribution<Byte, Integer> byteDistribution = new UniformByteDistribution();
        //When
        final DataDistributionAssessment<Byte> dataDistributionAssessment = assessDistribution(byteDistribution, 256);
        //Then
        byte value = -127;

        Assert.assertThat(dataDistributionAssessment.proportionLessThanOrEqualTo((byte)-65), is(both(greaterThan(0.20D)).and(lessThan(0.30D))));
        Assert.assertThat(dataDistributionAssessment.proportionBetween((byte)-64, (byte) 64), is(both(greaterThan(0.40D)).and(lessThan(0.60D))));
        Assert.assertThat(dataDistributionAssessment.proportionGreaterThanOrEqualTo((byte)64), is(both(greaterThan(0.20D)).and(lessThan(0.30D))));
    }

}
