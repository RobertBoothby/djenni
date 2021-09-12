package com.robertboothby.djenni.dynamic;

import com.robertboothby.djenni.core.StreamableSupplier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Standard implementation of {@link DefaultSuppliers}, will be the one used if no override is supplied
 * {@link DefaultSuppliers#overrideInstance(DefaultSuppliers)}.
 */
public class DefaultSuppliersImpl extends DefaultSuppliers {

    private final Map<Class<?>, Map<String, Supplier<?>>> classAndPropertyDefaults = new HashMap<>();

    public static final StreamableSupplier<?> defaultObjectSupplier = () -> null;

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
        Map<String, Supplier<?>> supplierMap = classAndPropertyDefaults.computeIfAbsent(clazz, $ -> new HashMap<>());
        return (StreamableSupplier<T>) supplierMap
                .computeIfAbsent(property, $ -> { //If there is no default supplier for the specific class and property combination create one or find one.
                    if ("".equals(property)) { //If the value of property indicates that it is for any class return the default supplier.
                        return defaultObjectSupplier;
                    } else { //Else get whatever supplier there is for the class ignoring the property or the default supplier.
                        return supplierMap.getOrDefault("", defaultObjectSupplier);
                    }
                });
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
