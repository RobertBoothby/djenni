package com.robertboothby.djenni.dynamic;

import com.robertboothby.djenni.core.StreamableSupplier;

import java.util.function.Supplier;

/**
 * This defines the controct for a repository of default suppliers used by the DynamicSupplierBuilder. There are three
 * tiers of defaults, a default supplier for a class <em>and</em> named property, a default supplier for a class in
 * isolation or a supplier of nulls if no other default supplier can be found.
 *
 * Be careful with instantiating
 */
public abstract class DefaultSuppliers {

    private static volatile DefaultSuppliers instance;
    private static final Object instanceMutex = new Object();

    /**
     * Static default string used to key for NO_PROPERTY. Calling {@link #getSupplierForClassAndProperty(Class, String)}
     * with NO_PROPERTY in the String parameter is the equivalent of calling {@link #getSupplierForClass(Class)}.
     */
    public static final String NO_PROPERTY = "";

    /**
     * Get the singleton instance of DefaultSuppliers
     *
     * @return the singleton instance of DefaultSuppliers
     */
    public static DefaultSuppliers defaultSuppliers() {
        if(instance == null) {
            synchronized (instanceMutex) {
                instance = new DefaultSuppliersImpl();
            }
        }
        return instance;
    }

    /**
     * Override the instance of DefaultSuppliers used.
     * @param overrideInstance The new instance of DefaultSuppliers to use.
     */
    public static void overrideInstance(DefaultSuppliers overrideInstance) {
        synchronized (instanceMutex) {
            instance = overrideInstance;
        }
    }

    /**
     * Get the supplier for a class.
     *
     * @param clazz The class to get the supplier for.
     * @param <T>   The type being returned.
     * @return The default supplier for the given class.
     */
    public abstract <T> StreamableSupplier<T> getSupplierForClass(Class<T> clazz);

    /**
     * Get the supplier for a given property name and a given class.
     *
     * @param clazz    The class to get the supplier for.
     * @param property The property name to get the supplier for.
     * @param <T>      The type of the class and property.
     * @return The default supplier
     */
    public abstract <T> StreamableSupplier<T> getSupplierForClassAndProperty(Class<T> clazz, String property);

    /**
     * Sets the default supplier for a particular class. Will be superseded if a more specific supplier is specified for
     * class and propertyName.
     * @param clazz The class for which to definite the default supplier.
     * @param supplier The default supplier to use for the class.
     * @param <T> The type of the value supplied.
     */
    public abstract <T> void setClassSupplier(Class<T> clazz, StreamableSupplier<? extends T> supplier);

    /**
     * Sets the default supplier for a particular class and property name. Supersedes the default supplier for the class
     * in isolation when selecting using property name too.
     * @param clazz The class for which to definite the default supplier.
     * @param propertyName The name of the property for which to define the default supplier.
     * @param supplier The default supplier to use for the class.
     * @param <T> The type of the value supplied.
     */
    public abstract <T> void setClassAndPropertySupplier(Class<T> clazz, String propertyName, StreamableSupplier<? extends T> supplier);

    /**
     * Reset the DefaultSupplier to original defaults.
     */
    public abstract void reset();
}
