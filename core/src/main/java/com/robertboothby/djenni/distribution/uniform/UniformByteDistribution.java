package com.robertboothby.djenni.distribution.uniform;

import org.apache.commons.lang.NotImplementedException;
import com.robertboothby.djenni.distribution.Distribution;
import org.hamcrest.Description;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Generates an uniformly distributed <u>unsigned</u> Byte value that when represented as a standard signed byte will
 * overflow so 0 to 127 and 128 to 255 will be represented when overflowed to the signed byte type as 0 to 127 and -1
 * to -128
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 *
 * @author robertboothby
 * @deprecated
 */
public class UniformByteDistribution implements Distribution<Byte, Integer> {

    byte[] masks = new byte[]{(byte)0xff, 0x01, 0x03, 0x07, 0x0f, 0x1f, 0x3f, 0x7f, (byte)0xff};

    public Byte generate(Integer bound) {
        ThreadLocalRandom.current().nextInt(bound);
        int numbits = 32 - Integer.numberOfLeadingZeros(bound);
        if(numbits >  8) {
            numbits = 8;
            bound = 256;
        }
        byte[] nextByte = new byte[1];
        int potentialResult;
        do {
            ThreadLocalRandom.current().nextBytes(nextByte);
            potentialResult = (nextByte[0] & masks[numbits]);
        } while(potentialResult >= bound);
        return (byte) potentialResult;

    }

    public void describeTo(Description description) {
        //TODO Implement...
        throw new NotImplementedException();
    }
}
