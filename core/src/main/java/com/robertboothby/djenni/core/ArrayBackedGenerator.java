package com.robertboothby.djenni.core;

import com.robertboothby.djenni.SerializableGenerator;
import org.apache.commons.lang.NotImplementedException;
import org.hamcrest.Description;

import java.util.Arrays;

/**
 * Simple Generator that randomly generates values from an array selected using the passed in Integer Generator.
 */
public class ArrayBackedGenerator<T> implements SerializableGenerator<T> {

    private final T[] array;
    private final SerializableGenerator<Integer> selectionGenerator;

    /**
     * Construct an instance of the generator using the passed in array and a generator configured to select values
     * from the array as desired. A shallow copy is made of the array but if full immutability is desired then ensure
     * that the entries in the array are properly managed or immutable themselves.
     * @param array the array from which to select values.
     * @param selectionGenerator the generator that will produce array indexes to select the values to be generated.
     */
    public ArrayBackedGenerator(T[] array, SerializableGenerator<Integer> selectionGenerator) {
        this.array = Arrays.copyOf(array, array.length); //shallow copy...
        this.selectionGenerator = selectionGenerator;
    }

    @Override
    public T generate() {
        return array[selectionGenerator.generate()];
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("ArrayBackedGenerator : {");
        description.appendValueList("values : [", ", ", "],", array);
        description.appendDescriptionOf(selectionGenerator);
        description.appendText("}");
    }
}
