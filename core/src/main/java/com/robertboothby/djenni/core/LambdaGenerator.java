package com.robertboothby.djenni.core;

import com.robertboothby.djenni.Generator;
import org.hamcrest.Description;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Lambda based skeleton for simple generators.
 * @param <T> The type of the value to be generated.
 */
public class LambdaGenerator<T> implements Generator<T> {

    private final Supplier<T> valueSupplier;
    private final Consumer<Description> descriptionConsumer;

    /**
     * Create a generator based on the supplied lambda functions.
     * @param valueSupplier The supplier to use for the values.
     * @param descriptionConsumer The consumer that will populate a description.
     */
    public LambdaGenerator(Supplier<T> valueSupplier, Consumer<Description> descriptionConsumer) {
        this.valueSupplier = valueSupplier;
        this.descriptionConsumer = descriptionConsumer;
    }

    @Override
    public T generate() {
        return valueSupplier.get();
    }

    @Override
    public void describeTo(Description description) {
        descriptionConsumer.accept(description);
    }

    /**
     * Construct an instance of the Lambda generator based on the supplied lambdas.
     * @param valueSupplier The supplier of values for the generator..
     * @param descriptionConsumer The consumer that will populate a description.
     * @param <T> The type of the value to be generated.
     * @return a generator that uses the lambdas.
     */
    public static <T> Generator<T> generator(Supplier<T> valueSupplier, Consumer<Description> descriptionConsumer) {
        return new LambdaGenerator<>(valueSupplier, descriptionConsumer);
    }
}
