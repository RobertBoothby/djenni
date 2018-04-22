package com.robertboothby.djenni.sugar;

/**
 * Interface to allow simple syntactic sugar when using functional language structures. This will allow
 * enforcing of binary parameters (e.g. between...and....) in a clear syntax.
 * @author robertboothby
 * @param <T> The configurable class that will be returned for further use.
 * @param <U> The class of the value being configured.
 */
public interface And<T, U> {

    /**
     * Set the second of a binary value into a configurable object.
     * @param value The value to be configured into the  object.
     * @return The object being configured
     */
    T and(U value);
}
