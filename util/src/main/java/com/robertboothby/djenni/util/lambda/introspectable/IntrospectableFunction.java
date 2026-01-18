package com.robertboothby.djenni.util.lambda.introspectable;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Function;

/**
 * <p>
 * An introspectable Lambda function for a class level getter (or similar method) that enables the retrieval of the method's
 * implementation class, method name, and return type.
 * </p>
 * <p>
 * It does this by leveraging Java's SerializedLambda mechanism to extract the necessary information from the Lambda
 * expression. While the SerializedLambda functionality can be unreliable if used to serialize between different JVM
 * versions or class loaders, it provides a reliable way to introspect Lambda expressions at runtime within the current
 * JVM.
 * </p>
 */
@FunctionalInterface
public interface IntrospectableFunction<T,R> extends Function<T,R>, Serializable {

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
        try {
            Method writeReplace = getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);
            return (SerializedLambda) writeReplace.invoke(this);
        } catch (Exception e) {
            throw new LambdaIntrospectionException(e);
        }
    }

}
