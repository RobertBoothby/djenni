package com.robertboothby.djenni.experimental;

/**
 * This exception is thrown when there are problems introspecting a Lambda using this framework.
 */
public class LambdaIntrospectionException extends RuntimeException {
    public LambdaIntrospectionException(Throwable cause) {
        super(cause);
    }
}
