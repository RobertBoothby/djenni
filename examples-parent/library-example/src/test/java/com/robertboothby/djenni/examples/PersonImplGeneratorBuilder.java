package com.robertboothby.djenni.examples;

import com.robertboothby.djenni.core.CharacterStrings;
import com.robertboothby.djenni.core.GeneratorHelper;
import com.robertboothby.djenni.lang.StringGeneratorBuilder;
import com.robertboothby.djenni.util.Nullable;
import com.robertboothby.djenni.Generator;

import static com.robertboothby.djenni.core.CharacterStrings.ENGLISH_ALPHABETIC_LOWER;
import static com.robertboothby.djenni.core.CharacterStrings.ENGLISH_ALPHABETIC_UPPER;
import static com.robertboothby.djenni.core.ExplicitlyBiassedGeneratorBuilder.explicitlyBiassedGeneratorFor;
import static com.robertboothby.djenni.core.GeneratorHelper.buildA;
import static com.robertboothby.djenni.core.GeneratorHelper.fixedValue;
import static com.robertboothby.djenni.examples.Gender.*;
import static com.robertboothby.djenni.lang.StringGeneratorBuilder.generatorOfArbitraryStrings;
import static com.robertboothby.djenni.util.Nullable.nullable;

/**
 * <p>&#169; 2014 Forest View Developments Ltd.</p>
 *
 * @author robertboothby
 */
public class PersonImplGeneratorBuilder extends AbstractPersonImplGeneratorBuilder {
    @Override
    public void setupDefaults() {
        this.firstNameConstructorGenerator =
                buildA(StringGeneratorBuilder.generatorOfArbitraryStrings()
                    .withAvailableCharacters(CharacterStrings.ENGLISH_ALPHABETIC_LOWER + CharacterStrings.ENGLISH_ALPHABETIC_UPPER));
        this.lastNameConstructorGenerator =
                buildA(StringGeneratorBuilder.generatorOfArbitraryStrings()
                    .withAvailableCharacters(CharacterStrings.ENGLISH_ALPHABETIC_LOWER + CharacterStrings.ENGLISH_ALPHABETIC_UPPER));
        this.SSNConstructorGenerator =
                buildA(StringGeneratorBuilder.generatorOfArbitraryStrings()
                    .withAFixedLengthOf(22)
                        .withEuropeanNumerals());
        this.genderConstructorGenerator =
                buildA(explicitlyBiassedGeneratorFor(Gender.class)
                        .addValue(Gender.FEMALE)
                        .addValue(Gender.MALE)
                        .addValue(Gender.OTHER, 0.01D)
                );
    }

    public static PersonImplGeneratorBuilder generatorOfPeople() {
        return new PersonImplGeneratorBuilder();
    }

    public PersonImplGeneratorBuilder male() {
        this.genderConstructorGenerator = GeneratorHelper.fixedValue(Gender.MALE);
        return this;
    }

    public PersonImplGeneratorBuilder female() {
        this.genderConstructorGenerator = GeneratorHelper.fixedValue(Gender.FEMALE);
        return this;
    }

    public PersonImplGeneratorBuilder otherGender() {
        this.genderConstructorGenerator = GeneratorHelper.fixedValue(Gender.OTHER);
        return this;
    }

    public PersonImplGeneratorBuilder withGenderGenerator(Generator<Gender> genderGenerator) {
        this.genderConstructorGenerator = genderGenerator;
        this.genderSetterGenerator = Nullable.nullable(genderGenerator);
        return this;
    }


}
