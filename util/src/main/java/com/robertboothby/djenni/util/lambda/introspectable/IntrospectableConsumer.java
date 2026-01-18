package com.robertboothby.djenni.util.lambda.introspectable;

import java.io.Serializable;
import java.lang.invoke.MethodHandleInfo;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * An introspectable Lambda function for a single-argument consumer.
 *
 * <p>This is typically used with either a static one-argument method reference (for example,
 * {@code MyType::acceptValue}) or a bound instance method reference (for example, {@code instance::acceptValue}).
 * For bound instance references, the captured instance can be recovered in-process via
 * {@link #getBoundInstance()}.</p>
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
