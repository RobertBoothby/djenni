package com.robertboothby.djenni.examples;

import org.djenni.Generator;
import org.djenni.SerializableGenerator;
import org.junit.Test;

import static org.djenni.core.ConcatenatingStringGeneratorBuilder.generatorOfConcatenatedValues;
import static org.djenni.core.GeneratorHelper.buildA;
import static org.djenni.examples.StaffNumberGeneratorBuilder.generatorOfValidStaffNumberStrings;
import static org.djenni.lang.StringGeneratorBuilder.generatorOfArbitraryStrings;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * <p>&#169; 2014 Forest View Developments Ltd.</p>
 *
 * @author robertboothby
 */
public class StaffNumberTest {


    /**
     * Every time this test is run another (supposedly) valid staff number is generated and checked.
     */
    @Test
    public void shouldHandleValidStaffNumber() {
        //Given
        final Generator<String> validStaffNumberGenerator = buildA(StaffNumberGeneratorBuilder.generatorOfValidStaffNumberStrings());

        //When
        final String stringStaffNumber = validStaffNumberGenerator.generate();
        StaffNumber staffNumber = new StaffNumber(stringStaffNumber);

        //Then
        assertThat(staffNumber.getStringStaffNumber(), is(stringStaffNumber));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailInvalidNumberWithNoAlpha() {
        //Given
        final Generator<String> inValidStaffNumberGenerator = buildA(
                generatorOfArbitraryStrings().withEuropeanNumerals().withAFixedLengthOf(7)
        );

        //When
        final String stringStaffNumber = inValidStaffNumberGenerator.generate();
        new StaffNumber(stringStaffNumber);

        //Then
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailInvalidNumberWithNoNumeric() {
        //Given
        final Generator<String> inValidStaffNumberGenerator = buildA(
                generatorOfArbitraryStrings().withUpperCaseEnglishAlphabet().withAFixedLengthOf(7)
        );

        //When
        final String stringStaffNumber = inValidStaffNumberGenerator.generate();
        new StaffNumber(stringStaffNumber);

        //Then
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWithOtherUnexpectedCharacters() {
        //Given
        final SerializableGenerator<String> prefixGenerator = buildA(generatorOfArbitraryStrings().withAvailableCharacters("!@£$$%^&*()").withAFixedLengthOf(3));
        final SerializableGenerator<String> suffixGenerator = buildA(generatorOfArbitraryStrings().withAvailableCharacters("!@£$$%^&*()").withAFixedLengthOf(4));

        final Generator<String> inValidStaffNumberGenerator = buildA(generatorOfConcatenatedValues()
                        .with(prefixGenerator).and(suffixGenerator));

        //When
        final String stringStaffNumber = inValidStaffNumberGenerator.generate();
        new StaffNumber(stringStaffNumber);

        //Then

    }
}
