package com.robertboothby.djenni.core;

import com.robertboothby.djenni.SerializableGenerator;
import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;

/**
 * A generator that will always return the configured fixed value no matter what. This is an important type of Generator
 * as it is one of the key generators for delivering fine grained control of the generation.
 * @author robertboothby
 * @param <T> The type of the fixed value.
 */
public class FixedValueGenerator<T> implements SerializableGenerator<T> {

    private final T fixedValue;
    /**
     * Construct a generator that will always return the same fixed value.
     * @param fixedValue the fixed value that will always be returned.
     */
    public FixedValueGenerator(T fixedValue) {
        this.fixedValue = fixedValue;
    }

    /**
     * Return the fixed value.
     * @return the fixed value.
     */
    public T generate() {
        return fixedValue;
    }

    public void describeTo(Description description) {
        description
                .appendText("{ FixedValueGenerator : { fixedValue : ");
        if(fixedValue instanceof SelfDescribing){
            description.appendDescriptionOf((SelfDescribing) fixedValue);
        } else {
            description.appendValue(fixedValue);
        }
        description.appendText(" } }");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FixedValueGenerator)) return false;

        FixedValueGenerator that = (FixedValueGenerator) o;

        if (!fixedValue.equals(that.fixedValue)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return fixedValue.hashCode();
    }

    @Override
    public String toString() {
        StringDescription description = new StringDescription();
        description.appendDescriptionOf(this);
        return description.toString();
    }
}
