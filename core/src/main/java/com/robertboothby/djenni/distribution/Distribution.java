package com.robertboothby.djenni.distribution;

/**
 * Abstraction for pluggable distributions that produce pseudo-random numbers for supplier builders. Implementations take
 * a numeric upper bound and return a value in the half-open interval {@code [0, bound)}. Using strongly-typed bounds
 * makes it possible to share the same distribution across primitive families (e.g. doubles backing integer sampling).
 *
 * @param <T> the numeric type generated when the distribution is used (e.g. {@link Double}, {@link Integer})
 * @param <U> the numeric type accepted as the bound (usually matches {@code T} but may differ for “full range” generators)
 */
public interface Distribution<T  extends Number, U extends Number> {

    /**
     * Produce the next value constrained to {@code [0, bound)}. Implementations should treat {@code bound} as exclusive
     * and may throw {@link IllegalArgumentException} if the bound is zero or negative.
     * @param bound exclusive upper bound on the value to produce
     * @return the next generated value
     */
    T generate(U bound);
}
