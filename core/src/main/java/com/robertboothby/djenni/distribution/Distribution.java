package com.robertboothby.djenni.distribution;

/**
 * <p>Interface for distributions, objects that provide sources of randomness for Suppliers.</p>
 *
 * @param <T> the type that is generated when the distribution is used.
 * @param <U> the type that is used to express the bound.
 * @author robertboothby
 */
public interface Distribution<T  extends Number, U extends Number> {

    T generate(U bound);
}
