package com.robertboothby.djenni.core;

import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.distribution.simple.SimpleRandomIntegerDistribution;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.robertboothby.djenni.core.ExplicitlyBiassedSupplierBuilder.explicitlyBiassedSupplierFor;
import static com.robertboothby.djenni.distribution.simple.SimpleRandomIntegerDistribution.UNIFORM;
import static com.robertboothby.djenni.lang.IntegerSupplierBuilder.integerSupplier;

/**
 * Helpful utilities for working with Suppliers.
 * @author robertboothby
 */
public class SupplierHelper {

    /**
     * Get a Supplier instance that will return a completely fixed value for all time. It will return the exact object.
     * @param fixedValue the fixed value that will always be returned.
     * @param <T> The type of the fixed value and so the supplier.
     * @return a Supplier that will always return the fixed value.
     */
    public static <T> StreamableSupplier<T> fix(final T fixedValue){
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
    public static <T> StreamableSupplier<T> fromValues(T ...  values) {
        return fromValues(UNIFORM, values);
    }

    /**
     * Create a supplier from a collection with an equal chance of generating any of the values.
     * @see SimpleRandomIntegerDistribution#UNIFORM
     * @param values The collection of values.
     * @param <T> The type of the values in the collection and being returned.
     * @return A supplier supplying values from the collection.
     */
    @SuppressWarnings("unchecked")
    public static <T> StreamableSupplier<T> fromCollection(Collection<T> values){
        return derived($ -> (T)$, fromValues(values.toArray()));
    }

    /**
     * Create a Supplier from a char array with equal chance of generating any of the values.
     * @see SimpleRandomIntegerDistribution#UNIFORM
     * @param values the values to supply
     * @return a Supplier derived from the values.
     */
    public static StreamableSupplier<Character> fromValues(char ...  values) {
        return fromValues(UNIFORM, values);
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
    public static <T> StreamableSupplier<T> fromValues(Distribution<Integer, Integer> distribution, T ... values) {
        Supplier<Integer> selectionGenerator = buildAn(integerSupplier()
                .between(0)
                .and(values.length)
                .withDistribution(distribution));
        T[] copy = copy(values);
        return () -> copy[selectionGenerator.get()];
    }

    /**
     * Create a supplier from an array using the passed in distribution to control the likelihood of generating any of
     * the values.
     * @param array the array to use when selecting values to generate
     * @param distribution the distribution to use when selecting any of the values from the array. Any bias introduced
     *                     relates to the position in the array that is selected.
     * @return a Supplier derived from the array.
     */
    public static StreamableSupplier<Character> fromValues(Distribution<Integer, Integer> distribution, char ... array) {
        Character[] characters = IntStream.range(0, array.length).mapToObj(i -> array[i]).toArray(Character[]::new);
        return fromValues(distribution, characters);
    }

    /**
     * Shortcut method for creating a Supplier that randomly generates values from the enumeration with equal bias.
     * @param enumerationClass The enumeration class to use.
     * @param <T> The enumeration type.
     * @return a generator that will generate values from the enumeration.
     */
    public static <T extends Enum<T>> StreamableSupplier<T> fromEnum(Class<T> enumerationClass) {
        return fromEnum(enumerationClass, UNIFORM);
    }

    /**
     * Shortcut method for creating a Supplier that randomly generates values from the enumeration with bias defined by
     * the passed in distribution.
     * @param enumerationClass The enumeration class to use.
     * @param distribution The distribution to use.
     * @param <T> The enumeration type.
     * @return a Supplier that will generate values from the enumeration.
     */
    public static <T extends Enum<T>> StreamableSupplier<T> fromEnum(Class<T> enumerationClass, Distribution<Integer, Integer> distribution) {
        return fromValues(distribution, enumerationClass.getEnumConstants());
    }

    /**
     * Uses a SupplierBuilder to build an instance of Suppler.
     * Purest syntactic sugar making it easy to write something approaching an english sentence when using the framework.
     * @param builder The builder to use to create the supplier.
     * @param <T> The type of the value that the supplier will produce.
     * @return A Supplier that has been built by the SupplierBuilder.
     */
    public static <T> StreamableSupplier<T> buildA(SupplierBuilder<T> builder){
        return builder.build();
    }

    /**
     * Uses a SupplierBuilder to build an instance of Suppler.
     * Purest syntactic sugar making it easy to write something approaching an english sentence when using the framework.
     * @param builder The builder to use to create the supplier.
     * @param <T> The type of the value that the supplier will produce.
     * @return A Supplier that has been built by the SupplierBuilder.
     */
    public static <T> StreamableSupplier<T> buildAn(SupplierBuilder<T> builder){
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
    public static <T, R> StreamableSupplier<R> derived(final Function<T, R> derivation, final Supplier<? extends T> supplier) {
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
    public static <T, U, R> StreamableSupplier<R> derived(final BiFunction<T, U, R> derivationFunction, final Supplier<? extends T> firstSupplier, final Supplier<? extends U> secondSupplier){
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

    /**
     * Create a supplier that picks between one of a range of suppliers.
     * @param suppliers The suppliers to choose from.
     * @param <T> The type of value to be supplied.
     * @return A supplier that randomly picks one of the suppliers to create the value.
     */
    @SafeVarargs
    public static <T> StreamableSupplier<T> fromRandomSuppliers(Supplier<T> ... suppliers){
        return fromRandomSuppliers(integerSupplier($ -> $.between(0).and(suppliers.length)), suppliers);
    }

    /**
     * Create a supplier that picks between one of a range of suppliers.
     * @param positionSupplier A supplier that provides an integer value to choose between the range of suppliers.
     * @param suppliers The suppliers to choose from.
     * @param <T> The type of value to be supplied.
     * @return A supplier that randomly picks one of the suppliers to create the value.
     */
    @SafeVarargs
    public static <T> StreamableSupplier<T> fromRandomSuppliers(Supplier<Integer> positionSupplier, Supplier<T> ... suppliers) {
        Supplier<T>[] suppliersCopy = copy(suppliers);
        return () -> suppliersCopy[positionSupplier.get()].get();
    }

    @SafeVarargs
    public static <T> T[] copy(T ... values){
        return Arrays.copyOf(values, values.length);
    }

    public static char[] copy(char ... values){
        return Arrays.copyOf(values, values.length);
    }

    /**
     * <p>
     *     Create a supplier wrapping another supplier that allows you to 'peek' at the values as they are supplied. It is
     *     strongly advised that you do not alter the values as you peek at them as this may cause thread safety and other
     *     issues.
     * </p>
     * <p>
     *     This can be used to create a set of linked values within a domain model but I have already provided an implementation
     *     {@link LinkableSupplier}.
     * </p>
     * @param supplier The supplier to wrap and peek at.
     * @param peeker The consumer that will peek at the values created by the wrapped supplier
     * @param <T> The type of the values being supplied.
     * @return A supplier that will allow peeking at the values.
     */
    public static <T> StreamableSupplier<T> peek(Supplier<? extends T> supplier, Consumer<T> peeker) {
        return () -> {
            T value = supplier.get();
            peeker.accept(value);
            return value;
        };
    }

    /**
     * Turn a Supplier into a StreamableSupplier either by casting or by creating a StreamableSupplier instance.
     * @param supplier the Supplier to use.
     * @param <T> The type of values to be supplied.
     * @return A StreamableSupplier derived from the passed in supplier.
     */
    public static <T> StreamableSupplier<T> asStreamable(Supplier<T> supplier){
        if(supplier instanceof StreamableSupplier){ //Cast it.
            return (StreamableSupplier<T>) supplier;
        } else { //Make is a Streamable.
            return supplier::get;
        }
    }

    /**
     * Get an instance of a Supplier that uses an instance of an underlying supplier per thread. This is used to make
     * a non-thread safe supplier thread safe.
     * @param instanceSupplier The supplier of suppliers used to instantiate the Thread Local... I loved typing that!
     * @param <T> The type of the values to be returned.
     * @return A configured, thread safe supplier.
     */
    public static <T> ThreadLocalSupplier<T> threadLocal(Supplier<Supplier<T>> instanceSupplier){
        return new ThreadLocalSupplier<>(instanceSupplier);
    }

    /**
     * Takes a supplier and make it randomly return null values.
     * @param supplier The underlying supplier.
     * @param nullFrequency the frequency to supply nulls defined as a double value between 0.0 (never) and 1.0 (always).
     * @param <T> The type of value being supplied.
     * @return A new StreamableSupplier that will randomly return null values.
     */
    public static <T> StreamableSupplier<T> nulls(Supplier<T> supplier, Double nullFrequency){
        return explicitlyBiassedSupplierFor(
                $ -> $
                        .addSupplier(() -> null, nullFrequency).addSupplier(supplier, 1.0D - nullFrequency)
        );
    }

}
