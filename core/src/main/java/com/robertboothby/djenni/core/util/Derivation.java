package com.robertboothby.djenni.core.util;

import com.robertboothby.djenni.Generator;
import org.hamcrest.SelfDescribing;

/**
 * This interface represents any transformative derivation of a value. it is intended to be used with the
 * {@link com.robertboothby.djenni.core.GeneratorHelper#derivedValueGenerator(Derivation, Generator)} utility method
 * to allow a derivation from the generated value.
 */
public interface Derivation<T, U> extends SelfDescribing {

    /**
     * Derive the value from the input.
     * @param input The input from which to derive a value;
     * @return the derived value.
     */
    public T derive(U input);
}
