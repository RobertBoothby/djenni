package com.robertboothby.djenni;

/**
 * A builder of object generators, implementing the &apos;Builder&apos; pattern. Instances of this interface will usually
 * be not thread safe and highly mutable.
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 * @author robertboothby
 * @param <T>
 */
public interface GeneratorBuilder <T> {

    /**
     * Build an instance of the generator based on the current configuration state.
     * @return an instance of the generator.
     */
    public Generator<T> build();
}
