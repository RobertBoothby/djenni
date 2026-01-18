package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.core.CharacterStrings;
import com.robertboothby.djenni.core.util.Collections;
import com.robertboothby.djenni.helper.DataCompletenessAssessment;
import com.robertboothby.djenni.matcher.Matchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static com.robertboothby.djenni.core.util.Collections.range;
import static com.robertboothby.djenni.core.SupplierHelper.buildA;
import static com.robertboothby.djenni.lang.CharacterSupplierBuilder.characterSupplier;
import static com.robertboothby.djenni.lang.IntegerSupplierBuilder.integerSupplier;
import static com.robertboothby.djenni.lang.StringSupplierBuilder.arbitraryString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 *
 * @author robertboothby
 */
public class StringSupplierBuilderTest {

    @Test
    public void shouldEventuallyGenerateAllLengths(){
        Supplier<String> stringGenerator = buildA(arbitraryString().withLengthsBetween(5).and(10));
        MatcherAssert.assertThat(stringGenerator,
                Matchers.eventuallySuppliesAllDerivatives(
                        range(5, 10),
                        String::length,
                        100
                )
        );
    }

    @Test public void shouldEventuallyUseAllCharacters(){
        Supplier<String> stringGenerator = buildA(arbitraryString().withEuropeanNumerals().withFixedLength(1));
        MatcherAssert.assertThat(stringGenerator,
                Matchers.eventuallySuppliesAllDerivatives(
                        Collections.asSetOfCharacters(CharacterStrings.EUROPEAN_NUMERIC),
                        original -> original.charAt(0),
                        100
                )
        );
    }

    @Test
    public void builtStringSuppliersShouldRemainStableAfterBuilderChanges() {
        StringSupplierBuilder builder = arbitraryString()
                .withFixedLength(1)
                .withAvailableCharacters("A");

        Supplier<String> supplier = buildA(builder);

        builder.withAvailableCharacters("BC");

        assertThat(supplier.get(), is("A"));
    }
}
