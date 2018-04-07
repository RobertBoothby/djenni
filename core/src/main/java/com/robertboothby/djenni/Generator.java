package com.robertboothby.djenni;

import org.hamcrest.SelfDescribing;

/**
 * A generator of objects intended primarily to facilitate the creation of test data. Instances of this interface
 * should typically be thread safe and immutable.
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 * @author robertboothby
 * @param <T> The type of object that is generated.
 */
public interface Generator<T> extends SelfDescribing {

    /**
     * Generate instances of the required class.
     * @return generated instances of the required class.
     */
    public T generate();

}
