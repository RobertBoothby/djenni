package com.robertboothby.djenni.core;

import com.robertboothby.djenni.Generator;
import com.robertboothby.djenni.GeneratorBuilder;
import com.robertboothby.djenni.SerializableGenerator;
import com.robertboothby.djenni.SerializableGeneratorBuilder;
import com.robertboothby.djenni.core.util.Derivation;
import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.distribution.simple.SimpleRandomIntegerDistribution;
import com.robertboothby.djenni.lang.IntegerGeneratorBuilder;
import org.hamcrest.Description;

/**
 * Helpful utilities for working with Generators.
 * @author robertboothby
 */
public class GeneratorHelper {

    /**
     * Get a generator instance that will return a completely fixed value for all time. It will return the exact object.
     * @param fixedValue the fixed value that will always be returned.
     * @param <T> The type of the fixed value and so the generator.
     * @return a generator that will always return the fixed value.
     */
    public static <T> SerializableGenerator<T> $(final T fixedValue){
        return new FixedValueGenerator<T>(fixedValue);
    }

    /**
     * Create a Generator from an array with equal chance of generating any of the values.
     * @see SimpleRandomIntegerDistribution#UNIFORM
     * @see ArrayBackedGenerator
     * @param array the array to use when selecting values to generate
     * @param <T> the type of values in the array and the generator.
     * @return a generator derived from the array.
     */
    public static <T> SerializableGenerator<T> fromArray(T[] array) {
        return fromArray(array, SimpleRandomIntegerDistribution.UNIFORM);
    }

    /**
     * Create a Generator from an array using the passed in distribution to control the likelihood of generating any of
     * the values.
     * @see ArrayBackedGenerator
     * @param array the array to use when selecting values to generate
     * @param distribution the distribution to use when selecting any of the values from the array. Any bias introduced
     *                     relates to the position in the array that is selected.
     * @param <T> the type of values in the array and the generator.
     * @return a generator derived from the array.
     */
    public static <T> SerializableGenerator<T> fromArray(T[] array, Distribution<Integer, Integer> distribution) {
        return new ArrayBackedGenerator<T>(
                array,
                buildAn(IntegerGeneratorBuilder.integerGenerator()
                        .between(0)
                        .and(array.length)
                        .withDistribution(distribution)));
    }

    /**
     * Shortcut method for creating a generator that randomly generates values from the enumeration with equal bias.
     * @param enumerationClass The enumeration class to use.
     * @param <T> The enumeration type.
     * @return a generator that will generate values from the enumeration.
     */
    public static <T extends Enum<T>> SerializableGenerator<T> fromEnum(Class<T> enumerationClass) {
        return fromArray(enumerationClass.getEnumConstants());
    }

    /**
     * Shortcut method for creating a generator that randomly generates values from the enumeration with equal bias.
     * @param enumerationClass The enumeration class to use.
     * @param distribution The distribution to use.
     * @param <T> The enumeration type.
     * @return a generator that will generate values from the enumeration.
     */
    public static <T extends Enum<T>> SerializableGenerator<T> fromEnum(Class<T> enumerationClass, Distribution<Integer, Integer> distribution) {
        return fromArray(enumerationClass.getEnumConstants(), distribution);
    }

    /**
     * Uses a GeneratorBuilder to build an instance of Generator.
     * Purest syntactic sugar making it easy to write something approaching an english sentence when using the framework.
     * @param builder The builder to use to create the generator.
     * @param <T> The type of the value that the generator will produce.
     * @return A generator that has been built by the GeneratorBuilder.
     */
    public static <T> Generator<T> buildA(GeneratorBuilder<T> builder){
        return builder.build();
    }

    /**
     * Uses a SerializableGeneratorBuilder to build an instance of Serializable Generator.
     * Purest syntactic sugar making it easy to write something approaching an english sentence when using the framework.
     * @param builder The builder to use to create the generator.
     * @param <T> The type of the value that the generator will produce.
     * @return A generator that has been built by the GeneratorBuilder.
     */
    public static <T> SerializableGenerator<T> buildA(SerializableGeneratorBuilder<T> builder){
        return builder.build();
    }

    /**
     * Uses a GeneratorBuilder to build an instance of Generator.
     * Purest syntactic sugar making it easy to write something approaching an english sentence when using the framework.
     * @param builder The builder to use to create the generator.
     * @param <T> The type of the value that the generator will produce.
     * @return A generator that has been built by the GeneratorBuilder.
     */
    public static <T> Generator<T> buildAn(GeneratorBuilder<T> builder){
        return builder.build();
    }

    /**
     * Uses a SerializableGeneratorBuilder to build an instance of SerializableGenerator.
     * Purest syntactic sugar making it easy to write something approaching an english sentence when using the framework.
     * @param builder The builder to use to create the generator.
     * @param <T> The type of the value that the generator will produce.
     * @return A generator that has been built by the GeneratorBuilder.
     */
    public static <T> SerializableGenerator<T> buildAn(SerializableGeneratorBuilder<T> builder){
        return builder.build();
    }

    public static <T, U> Generator<T> derivedValueGenerator(final Derivation<T,U> derivation, final Generator<? extends U> generator) {
        return new Generator<T>() {
            @Override
            public T generate() {
                return derivation.derive(generator.generate());
            }

            @Override
            public void describeTo(Description description) {
                description
                        .appendText("{ Derived Value Generator : { derivation = ")
                        .appendDescriptionOf(derivation)
                        .appendText(" , generator = ")
                        .appendDescriptionOf(generator)
                        .appendText("} } ");
            }
        };
    }
}
