package com.robertboothby.djenni.lang;

import org.apache.commons.lang.NotImplementedException;
import com.robertboothby.djenni.SerializableGenerator;
import org.hamcrest.Description;

/**
 * This particular generator of strings is intended to facilitate the building of structured / formatted strings. Of
 * particular use in generating Date, Time, Numeric and &quot;Identifier$quot; strings.
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 * @author robertboothby
 */
public class StructuredStringGenerator implements SerializableGenerator<String> {
    public String generate() {
        //TODO implement.
        throw new NotImplementedException();
    }

    public void describeTo(Description description) {
        throw new NotImplementedException();
    }
}
