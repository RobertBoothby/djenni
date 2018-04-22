package com.robertboothby.djenni.lang;

import org.apache.commons.lang3.NotImplementedException;
import com.robertboothby.djenni.SerializableGenerator;
import org.hamcrest.Description;

/**
 *
 * @author robertboothby
 */
public class RandomFloatGenerator implements SerializableGenerator<Float> {
    public Float generate() {
        //TODO implement
        throw new NotImplementedException("Needs implementation.");
    }

    public void describeTo(Description description) {
        throw new NotImplementedException("Needs implementation.");
    }
}
