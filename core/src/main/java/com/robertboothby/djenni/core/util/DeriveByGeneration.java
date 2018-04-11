package com.robertboothby.djenni.core.util;

import com.robertboothby.djenni.Generator;
import org.hamcrest.Description;

/**
 * Simple implementation of the Derivation interface that simply generates a value to do the derivation
 */
public class DeriveByGeneration<T,U extends Generator<T>> implements Derivation<T, U> {

    @Override
    public T derive(U input) {
        return input.generate();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("DeriveByGeneration:  {}");
    }
}
