package com.robertboothby.djenni.lang.serializable;

import com.robertboothby.djenni.SerializableGenerator;
import com.robertboothby.djenni.lang.StringGenerator;

/**
 * Version of {@link StringGenerator} that guarantees serializability.
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 * @author robertboothby
 */
public class SerializableStringGenerator extends StringGenerator implements SerializableGenerator<String> {

    /**
     * Construct an instance of the generator that is guaranteed to be serializable.
     *
     * @param lengthGenerator            a generator that will provide the length of the generated string.
     * @param characterGenerator a generator that will provide the characters composing the string.
     */
    public SerializableStringGenerator(
            SerializableGenerator<Integer> lengthGenerator, SerializableGenerator<Character> characterGenerator) {
        super(lengthGenerator, characterGenerator);
    }



}
