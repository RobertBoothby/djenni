package com.robertboothby.djenni.util.lambda.introspectable;

/**
 * This exception is thrown when there are problems introspecting a Lambda using this framework.
 */
public class LambdaIntrospectionException extends RuntimeException {
    public LambdaIntrospectionException(Throwable cause) {
        super(cause);
    }
}
