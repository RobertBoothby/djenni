package com.robertboothby.djenni.dynamic;

import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.core.SupplierHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Standard implementation of {@link DefaultSuppliers}, will be the one used if no override is supplied
 * {@link DefaultSuppliers#overrideInstance(DefaultSuppliers)}.
 */
public class DefaultSuppliersImpl extends DefaultSuppliers {

    private final Map<Class<?>, Map<String, StreamableSupplier<?>>> classAndPropertyDefaults = new HashMap<>();

    public static final StreamableSupplier<?> defaultObjectSupplier = SupplierHelper.nullSupplier();

    { // set up defaults for the primitive types... nulls will break things.
        setPrimitiveDefaults();
    }

    private void setPrimitiveDefaults() {
        this.setClassSupplier(byte.class, () -> (byte) 0);
        this.setClassSupplier(short.class, () -> (short) 0);
        this.setClassSupplier(int.class, () -> 0);
        this.setClassSupplier(long.class, () -> 0L);
        this.setClassSupplier(float.class, () -> 0.0f);
        this.setClassSupplier(double.class, () -> 0.0d);
        this.setClassSupplier(boolean.class, () -> false);
        this.setClassSupplier(char.class, () -> '\u0000');
    }

    @Override
    public <T> StreamableSupplier<T> getSupplierForClass(Class<T> clazz) {
        return getSupplierForClassAndProperty(clazz, NO_PROPERTY);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> StreamableSupplier<T> getSupplierForClassAndProperty(Class<T> clazz, String property) {
        return (StreamableSupplier<T>) classAndPropertyDefaults
                .computeIfAbsent(clazz, $ -> new HashMap<>())
                .getOrDefault(property, classAndPropertyDefaults
                        .computeIfAbsent(clazz, $1 -> new HashMap<>())
                        .getOrDefault(NO_PROPERTY, defaultObjectSupplier));
    }

    @Override
    public <T> void setClassSupplier(Class<T> clazz, StreamableSupplier<? extends T> supplier) {
        setClassAndPropertySupplier(clazz, NO_PROPERTY, supplier);
    }

    @Override
    public <T> void setClassAndPropertySupplier(Class<T> clazz, String propertyName, StreamableSupplier<? extends T> supplier) {
        classAndPropertyDefaults.computeIfAbsent(clazz, $ -> new HashMap<>()).put(propertyName, supplier);
    }

    @Override
    public void reset() {
        classAndPropertyDefaults.clear();
        setPrimitiveDefaults();
    }
}
