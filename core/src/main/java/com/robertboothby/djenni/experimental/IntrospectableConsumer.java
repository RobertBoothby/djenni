package com.robertboothby.djenni.experimental;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * An introspectable Lambda function for a getter (or similar method).
 */
@FunctionalInterface
public interface IntrospectableConsumer<T> extends Consumer<T>, Serializable {

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
    default Class<T> getParameterType() {
        return (Class<T>) Arrays.stream(getImplementingClass()
                .getMethods())
                .filter($ -> $
                        .getName().equals(getMethodName())
                        &&
                        $.getParameterCount() == 1)
                .findFirst()
                .orElseThrow()
                .getParameterTypes()[0];
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
