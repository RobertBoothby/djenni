package com.robertboothby.djenni;

import java.io.Serializable;

/**
 * This is intended to be a marker interface for all Generator instances that are serializable so that the generator
 * configuration can be stored. My preference is to use a serialization mechanism such as XStream so that human-readable
 * configurations can be generated.
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 * @author robertboothby
 * @param <T> The type of the object to be generated.
 */
public interface SerializableGenerator<T> extends Generator<T>, Serializable  {
}
