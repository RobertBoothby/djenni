package com.robertboothby.djenni.dynamic;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * This serves as the repository of all default suppiers used by the DynamicSupplierBuilder. There are three tiers of defaults,
 * a default supplier for a class <em>and</em> named property, a default supplier for a class in isolation or a supplier
 * of nulls if no other default supplier can be found.
 *
 * TODO consider making the DefaultSuppliers an abstract class that can be overridden...
 */
public class DefaultSuppliers {

    /**
     * Static default string used to key for NO_PROPERTY. Calling {@link #getSupplierForClassAndProperty(Class, String)}
     * with NO_PROPERTY in the String parameter is the equivalent of calling {@link #getSupplierForClass(Class)}.
     */
    public static final String NO_PROPERTY = "";
    private static final DefaultSuppliers instance = new DefaultSuppliers();

    static { // set up defaults for the primitive types... nulls will break things.
        instance.setClassSupplier(byte.class, () -> (byte) 0);
        instance.setClassSupplier(short.class, () -> (short) 0);
        instance.setClassSupplier(int.class, () -> 0);
        instance.setClassSupplier(long.class, () -> 0L);
        instance.setClassSupplier(float.class, () -> 0.0f);
        instance.setClassSupplier(double.class, () -> 0.0d);
        instance.setClassSupplier(boolean.class, () -> false);
        instance.setClassSupplier(char.class, () -> '\u0000');
    }

    private DefaultSuppliers() {
    }

    public final Map<Class<?>, Map<String, Supplier<?>>> classAndPropertyDefaults = new HashMap<>();

    /**
     * Get the supplier for a class.
     *
     * @param clazz The class to get the supplier for.
     * @param <T>   The type being returned.
     * @return The default supplier for the given class.
     */
    public <T> Supplier<T> getSupplierForClass(Class<T> clazz) {
        return getSupplierForClassAndProperty(clazz, NO_PROPERTY);
    }

    /**
     * Get the supplier for a given property name and a given class.
     *
     * @param clazz    The class to get the supplier for.
     * @param property The property name to get the supplier for.
     * @param <T>      The type of the class and property.
     * @return The default supplier
     */
    @SuppressWarnings("unchecked")
    public <T> Supplier<T> getSupplierForClassAndProperty(Class<T> clazz, String property) {
        return (Supplier<T>) classAndPropertyDefaults.computeIfAbsent(clazz, $ -> new HashMap<>())
                .computeIfAbsent(property, $ -> { //If there is no default supplier for the specific class and property combination create one or find one.
                    if ("".equals(property)) { //If the value of property indicates that it is for any class return a null Supplier.
                        return () -> null;
                    } else { //Else get whatever supplier there is for the class ignoring the property.
                        return getSupplierForClassAndProperty(clazz, NO_PROPERTY); // return the default for the class
                    }
                });
    }

    public <T> void setClassSupplier(Class<T> clazz, Supplier<? extends T> supplier) {
        setClassAndPropertySupplier(clazz, NO_PROPERTY, supplier);
    }

    public <T> void setClassAndPropertySupplier(Class<T> clazz, String propertyName, Supplier<? extends T> supplier) {
        classAndPropertyDefaults.computeIfAbsent(clazz, $ -> new HashMap<>()).put(propertyName, supplier);
    }

    /**
     * Get the singleton instance of DefaultSuppliers
     * @return the singleton instance of DefaultSuppliers
     */
    public static DefaultSuppliers defaultSuppliers() {
        return instance;
    }
}
