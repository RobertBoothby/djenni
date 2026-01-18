package com.robertboothby.djenni.util.lambda.introspectable;

import java.io.Serializable;
import java.lang.invoke.MethodHandleInfo;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * <p>
 * An introspectable Lambda function for an instance level getter (or similar method) that enables the retrieval of the method's
 * implementation class, method name, and return type.
 * </p>
 * <p>
 * It does this by leveraging Java's SerializedLambda mechanism to extract the necessary information from the Lambda
 * expression. While the SerializedLambda functionality can be unreliable if used to serialize between different JVM
 * versions or class loaders, it provides a reliable way to introspect Lambda expressions at runtime within the current
 * JVM.
 * </p>
 * <p>
 * For bound instance method references (for example, {@code person::getName}), the captured instance can be
 * recovered in-process via {@link #getBoundInstance()}. This is intended for immediate, in-JVM introspection only and
 * should not be persisted or serialized across JVM boundaries.
 * </p>
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

    /**
     * @return true if the referenced method is static.
     */
    default boolean isStaticMethod() {
        return serializeToIntrospectableForm().getImplMethodKind() == MethodHandleInfo.REF_invokeStatic;
    }

    /**
     * @return true if this method reference is bound to a specific instance.
     */
    default boolean isBoundInstance() {
        return serializeToIntrospectableForm().getCapturedArgCount() == 1;
    }

    /**
     * Returns the captured instance for a bound instance method reference, if present.
     *
     * <p>This is intended for immediate, in-JVM introspection only. Do not persist or serialize the returned
     * instance across JVM boundaries.</p>
     *
     * @return the captured instance when bound, otherwise an empty Optional.
     */
    default Optional<Object> getBoundInstance() {
        SerializedLambda serialized = serializeToIntrospectableForm();
        if (serialized.getCapturedArgCount() == 1) {
            return Optional.ofNullable(serialized.getCapturedArg(0));
        }
        return Optional.empty();
    }

}
