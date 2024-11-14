package com.robertboothby.djenni.util.lambda.introspectable;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Supplier;

/**
 * An introspectable Lambda function for a getter (or similar method).
 */
@FunctionalInterface
public interface IntrospectableSupplier<T> extends Supplier<T>, Serializable {

    default Class getImplementingClass() {
        try {
            return Class.forName(serializeToIntrospectableForm().getImplClass().replace('/', '.'));
        } catch (ClassNotFoundException e) {
            throw new LambdaIntrospectionException(e);
        }
    }

    default String getMethodName() {
        return serializeToIntrospectableForm().getImplMethodName();
    }

    @SuppressWarnings("unchecked")
    default Class<T> getReturnType() {
        return (Class<T>) Arrays.stream(getImplementingClass()
                .getMethods())
                .filter($ -> $
                        .getName().equals(getMethodName())
                        &&
                        $.getParameterCount() == 0)
                .findFirst()
                .orElseThrow()
                .getReturnType();
    }

    default SerializedLambda serializeToIntrospectableForm() {
        Method writeReplace = null;
        try {
            writeReplace = getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);
            return (SerializedLambda) writeReplace.invoke(this);
        } catch (Exception e) {
            throw new LambdaIntrospectionException(e);
        }
    }

}
