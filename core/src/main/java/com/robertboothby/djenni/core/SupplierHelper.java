package com.robertboothby.djenni.core;

import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.distribution.simple.SimpleRandomIntegerDistribution;
import com.robertboothby.djenni.lang.IntegerSupplierBuilder;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Helpful utilities for working with Suppliers.
 * @author robertboothby
 */
public class SupplierHelper {

    /**
     * Get a Supplier instance that will return a completely fixed value for all time. It will return the exact object.
     * @param fixedValue the fix value that will always be returned.
     * @param <T> The type of the fix value and so the supplier.
     * @return a Supplier that will always return the fix value.
     */
    public static <T> Supplier<T> fix(final T fixedValue){
        return () -> fixedValue;
    }

    /**
     * Create a Supplier from an array with equal chance of generating any of the values.
     * @see SimpleRandomIntegerDistribution#UNIFORM
     * @param values the array to use when selecting values to supply.
     * @param <T> the type of values in the array and the supplier.
     * @return a Supplier derived from the array.
     */
    @SafeVarargs
    public static <T> Supplier<T> fromValues(T ...  values) {
        return fromValues(SimpleRandomIntegerDistribution.UNIFORM, values);
    }

    /**
     * Create a Supplier from a char array with equal chance of generating any of the values.
     * @see SimpleRandomIntegerDistribution#UNIFORM
     * @param values the values to supply
     * @return a Supplier derived from the values.
     */
    public static Supplier<Character> fromValues(char ...  values) {
        return fromValues(SimpleRandomIntegerDistribution.UNIFORM, values);
    }

    /**
     * Create a Supplier from an array using the passed in distribution to control the likelihood of generating any of
     * the values.
     * @param values the array to use when selecting values to generate
     * @param distribution the distribution to use when selecting any of the values from the array. Any bias introduced
     *                     relates to the position in the array that is selected.
     * @param <T> the type of values in the array and the supplier.
     * @return a supplier derived from the array.
     */
    @SafeVarargs
    public static <T> Supplier<T> fromValues(Distribution<Integer, Integer> distribution, T ... values) {
        Supplier<Integer> selectionGenerator = buildAn(IntegerSupplierBuilder.integerSupplier()
                .between(0)
                .and(values.length)
                .withDistribution(distribution));
        T[] copy = Arrays.copyOf(values, values.length);
        return () -> {
          return copy[selectionGenerator.get()];
        };
    }

    /**
     * Create a supplier from an array using the passed in distribution to control the likelihood of generating any of
     * the values.
     * @param array the array to use when selecting values to generate
     * @param distribution the distribution to use when selecting any of the values from the array. Any bias introduced
     *                     relates to the position in the array that is selected.
     * @return a Supplier derived from the array.
     */
    public static Supplier<Character> fromValues(Distribution<Integer, Integer> distribution, char ... array) {
        Character[] characters = IntStream.range(0, array.length).mapToObj(i -> array[i]).toArray(Character[]::new);
        return fromValues(distribution, characters);
    }

    /**
     * Shortcut method for creating a Supplier that randomly generates values from the enumeration with equal bias.
     * @param enumerationClass The enumeration class to use.
     * @param <T> The enumeration type.
     * @return a generator that will generate values from the enumeration.
     */
    public static <T extends Enum<T>> Supplier<T> fromEnum(Class<T> enumerationClass) {
        return fromValues(enumerationClass.getEnumConstants());
    }

    /**
     * Shortcut method for creating a Supplier that randomly generates values from the enumeration with bias defined by
     * the passed in distribution.
     * @param enumerationClass The enumeration class to use.
     * @param distribution The distribution to use.
     * @param <T> The enumeration type.
     * @return a Supplier that will generate values from the enumeration.
     */
    public static <T extends Enum<T>> Supplier<T> fromEnum(Class<T> enumerationClass, Distribution<Integer, Integer> distribution) {
        return fromValues(distribution, enumerationClass.getEnumConstants());
    }

    /**
     * Uses a SupplierBuilder to build an instance of Suppler.
     * Purest syntactic sugar making it easy to write something approaching an english sentence when using the framework.
     * @param builder The builder to use to create the supplier.
     * @param <T> The type of the value that the supplier will produce.
     * @return A Supplier that has been built by the SupplierBuilder.
     */
    public static <T> Supplier<T> buildA(SupplierBuilder<T> builder){
        return builder.build();
    }

    /**
     * Uses a SupplierBuilder to build an instance of Suppler.
     * Purest syntactic sugar making it easy to write something approaching an english sentence when using the framework.
     * @param builder The builder to use to create the supplier.
     * @param <T> The type of the value that the supplier will produce.
     * @return A Supplier that has been built by the SupplierBuilder.
     */
    public static <T> Supplier<T> buildAn(SupplierBuilder<T> builder){
        return builder.build();
    }

    /**
     * Create a supplier from a pre-existing supplier using an {@link Function}.
     * @param derivation The derivation function to use.
     * @param supplier The base supplier to which the function will be applied.
     * @param <T> The type of the value generated by the base supplier.
     * @param <R> The type of the value that will be generated by the new supplier using the derivation function.
     * @return A new supplier using the derivation function to create the new type.
     */
    public static <T, R> Supplier<R> derived(final Function<T, R> derivation, final Supplier<? extends T> supplier) {
        return () -> derivation.apply(supplier.get());
    }

    /**
     * Create a supplier from two pre-existing suppliers using a {@link BiFunction},
     * @param derivationFunction The BiFunction to use to put the supplied values to
     * @param firstSupplier The first supplier to use.
     * @param secondSupplier The second supplier to use.
     * @param <T> The type of values supplied by the first supplier.
     * @param <U> The type of values supplied by the second supplier.
     * @param <R> The type of value that will be returned from the new supplier.
     * @return the new supplier.
     */
    public static <T, U, R> Supplier<R> derived(final BiFunction<T, U, R> derivationFunction, final Supplier<? extends T> firstSupplier, final Supplier<? extends U> secondSupplier){
        return () -> derivationFunction.apply(firstSupplier.get(), secondSupplier.get());
    }

    /**
     * Create a stream from a given supplier.
     * @param supplier the supplier
     * @param numberOfValues the number of value to be generated
     * @param <T> The type of the stream.
     * @return a stream taking values from the supplier.
     */
    public static <T> Stream<T> stream(Supplier<T> supplier, long numberOfValues){
        return Stream.generate(supplier).limit(numberOfValues);
    }
}