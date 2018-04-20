package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.Generator;
import com.robertboothby.djenni.core.CharacterStrings;
import com.robertboothby.djenni.core.util.Collections;
import com.robertboothby.djenni.helper.DataCompletenessAssessment;
import com.robertboothby.djenni.matcher.Matchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static com.robertboothby.djenni.core.GeneratorHelper.buildA;
import static com.robertboothby.djenni.lang.StringGeneratorBuilder.arbitraryString;

/**
 *
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 * @author robertboothby
 */
public class StringGeneratorTest {

    @Test
    public void shouldEventuallyGenerateAllLengths(){
        Generator<String> stringGenerator = buildA(arbitraryString().withLengthsBetween(5).and(10));
        MatcherAssert.assertThat(stringGenerator,
            Matchers.eventuallyGeneratesAllDerivatives(
                Collections.range(5, 10),
                new DataCompletenessAssessment.Deriver<Integer, String>() {
                    public Integer derive(String original) {
                        return original.length();
                    }
                },
                100
            )
        );
    }

    @Test public void shouldEventuallyUseAllCharacters(){
        Generator<String> stringGenerator = buildA(arbitraryString().withEuropeanNumerals().withFixedLength(1));
        MatcherAssert.assertThat(stringGenerator,
            Matchers.eventuallyGeneratesAllDerivatives(
                Collections.asSet(CharacterStrings.EUROPEAN_NUMERIC.toCharArray()),
                new DataCompletenessAssessment.Deriver<Character, String>() {
                    public Character derive(String original) {
                        return original.charAt(0);
                    }
                },
                100
            )
        );
    }

}
