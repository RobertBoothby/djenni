package com.robertboothby.djenni.util;

import com.robertboothby.djenni.util.lambda.introspectable.IntrospectableFunction;

import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Utility for creating modified copies of Java records.
 */
public final class RecordCopyHelper {

    private static final ClassValue<RecordMetadata> RECORD_METADATA_CACHE = new ClassValue<>() {
        @Override
        protected RecordMetadata computeValue(Class<?> type) {
            return introspectRecord(type);
        }
    };

    private RecordCopyHelper() {
    }

    /**
     * Creates a builder for applying one or more changes to a record instance.
     *
     * @param original the record instance to copy.
     * @param <T>      the record type.
     * @return a builder that can apply changes and build a new record instance.
     * @throws NullPointerException if {@code original} is null.
     */
    public static <T extends Record> Builder<T> copyOf(T original) {
        Objects.requireNonNull(original, "original");
        return new Builder<>(original);
    }

    /**
     * Creates a copy of {@code original} with a single record component changed.
     *
     * <p>Null handling:</p>
     * <ul>
     *     <li>Null {@code original} or {@code accessor} is rejected.</li>
     *     <li>Null {@code newValue} is allowed for reference-typed components.</li>
     *     <li>Null {@code newValue} is rejected for primitive components.</li>
     * </ul>
     *
     * @param original the record instance to copy.
     * @param accessor the record accessor method reference for the component to change.
     * @param newValue the new value for the component.
     * @param <T>      the record type.
     * @param <P>      the component type.
     * @return a new record instance with the specified component updated.
     * @throws NullPointerException     if {@code original} or {@code accessor} is null.
     * @throws IllegalArgumentException if the accessor does not target the record type or no component matches.
     */
    public static <T extends Record, P> T copyWithChange(T original, IntrospectableFunction<T, P> accessor, P newValue) {
        Objects.requireNonNull(original, "original");
        Objects.requireNonNull(accessor, "accessor");

        Class<?> recordClass = original.getClass();
        if (!recordClass.isRecord()) {
            throw new IllegalArgumentException("Class is not a record: " + recordClass.getName());
        }

        Class<?> implementingClass = accessor.getImplementingClass();
        if (!recordClass.isAssignableFrom(implementingClass)) {
            throw new IllegalArgumentException("Accessor does not target record type " + recordClass.getName() +
                    " (was " + implementingClass.getName() + ")");
        }

        RecordMetadata metadata = RECORD_METADATA_CACHE.get(recordClass);
        Integer targetIndex = metadata.accessorIndex.get(accessor.getMethodName());
        if (targetIndex == null) {
            throw new IllegalArgumentException("No record component matches accessor " + accessor.getMethodName() +
                    " on " + recordClass.getName());
        }

        if (newValue == null && metadata.components[targetIndex].getType().isPrimitive()) {
            throw new IllegalArgumentException("Null value not allowed for primitive component " +
                    metadata.components[targetIndex].getName() + " on " + recordClass.getName());
        }

        Object[] arguments = new Object[metadata.components.length];
        for (int i = 0; i < metadata.components.length; i++) {
            RecordComponent component = metadata.components[i];
            if (i == targetIndex) {
                arguments[i] = newValue;
            } else {
                try {
                    arguments[i] = component.getAccessor().invoke(original);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to read record component " + component.getName() +
                            " from " + recordClass.getName(), e);
                }
            }
        }

        try {
            @SuppressWarnings("unchecked")
            Constructor<T> constructor = (Constructor<T>) metadata.constructor;
            return constructor.newInstance(arguments);
        } catch (Exception e) {
            throw new RuntimeException("Failed to construct record " + recordClass.getName(), e);
        }
    }

    public static final class Builder<T extends Record> {
        private final T original;

        private Builder(T original) {
            this.original = original;
        }

        /**
         * Returns a new builder with the given component updated. Null handling follows
         * {@link #copyWithChange(Record, IntrospectableFunction, Object)}.
         *
         * @param accessor the record accessor method reference for the component to change.
         * @param newValue the new value for the component.
         * @param <P>      the component type.
         * @return a new builder with the updated record instance.
         */
        public <P> Builder<T> with(IntrospectableFunction<T, P> accessor, P newValue) {
            return new Builder<>(RecordCopyHelper.copyWithChange(original, accessor, newValue));
        }

        /**
         * Builds and returns the updated record instance.
         *
         * @return the updated record instance.
         */
        public T build() {
            return original;
        }
    }

    private static RecordMetadata introspectRecord(Class<?> recordClass) {
        RecordComponent[] components = recordClass.getRecordComponents();
        if (components == null || components.length == 0) {
            throw new IllegalArgumentException("Record has no components: " + recordClass.getName());
        }

        Class<?>[] parameterTypes = new Class<?>[components.length];
        Map<String, Integer> accessorIndex = new HashMap<>();
        for (int i = 0; i < components.length; i++) {
            RecordComponent component = components[i];
            parameterTypes[i] = component.getType();
            accessorIndex.put(component.getAccessor().getName(), i);
        }

        try {
            Constructor<?> constructor = recordClass.getDeclaredConstructor(parameterTypes);
            return new RecordMetadata(components, constructor, accessorIndex);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Failed to locate record constructor for " + recordClass.getName(), e);
        }
    }

    private static final class RecordMetadata {
        private final RecordComponent[] components;
        private final Constructor<?> constructor;
        private final Map<String, Integer> accessorIndex;

        private RecordMetadata(RecordComponent[] components, Constructor<?> constructor, Map<String, Integer> accessorIndex) {
            this.components = components;
            this.constructor = constructor;
            this.accessorIndex = accessorIndex;
        }
    }
}
