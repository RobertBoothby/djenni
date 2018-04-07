package com.robertboothby.djenni.core.util;

import org.hamcrest.SelfDescribing;

/**
 *
 * <p>&#169; 2014 Forest View Developments Ltd.</p>
 *
 * @author robertboothby
 */
public interface Derivation<T, U> extends SelfDescribing {

    /**
     * Derive the value from the input.
     * @param input The input from which to derive a value;
     * @return the derived value.
     */
    public T derive(U input);
}
