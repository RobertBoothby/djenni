package com.robertboothby.djenni.examples;

import com.robertboothby.djenni.GeneratorBuilder;
import org.apache.commons.lang.NotImplementedException;
import org.djenni.Generator;
import org.djenni.GeneratorBuilder;
import org.djenni.SerializableGenerator;

import static org.djenni.core.ConcatenatingStringGeneratorBuilder.generatorOfConcatenatedValues;
import static org.djenni.core.GeneratorHelper.buildA;
import static org.djenni.lang.StringGeneratorBuilder.generatorOfArbitraryStrings;

/**
 * <p>&#169; 2014 Forest View Developments Ltd.</p>
 *
 * @author robertboothby
 */
public class StaffNumberGeneratorBuilder extends AbstractStaffNumberGeneratorBuilder {

    @Override
    public void setupDefaults() {
        this.stringStaffNumberConstructorGenerator = buildA(generatorOfValidStaffNumberStrings());
    }

    public static StaffNumberGeneratorBuilder generatorOfValidStaffNumbers() {
        return new StaffNumberGeneratorBuilder();
    }

    public static GeneratorBuilder<String> generatorOfValidStaffNumberStrings() {
        final SerializableGenerator<String> alphaPrefixGenerator = buildA(generatorOfArbitraryStrings().withUpperCaseEnglishAlphabet().withAFixedLengthOf(3));
        final SerializableGenerator<String> numericSuffixGenerator = buildA(generatorOfArbitraryStrings().withEuropeanNumerals().withAFixedLengthOf(4));

        return generatorOfConcatenatedValues()
                        .with(alphaPrefixGenerator).and(numericSuffixGenerator);

    }

}
