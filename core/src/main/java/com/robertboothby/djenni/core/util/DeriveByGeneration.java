package com.robertboothby.djenni.core.util;

import org.djenni.Generator;
import org.hamcrest.Description;

/**
 * <p>&#169; 2014 Forest View Developments Ltd.</p>
 *
 * @author robertboothby
 */
public class DeriveByGeneration<T,U extends Generator<T>> implements Derivation<T, U> {
    @Override
    public T derive(U input) {
        return input.generate();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("{ DeriveByGeneration }");

    }
}
