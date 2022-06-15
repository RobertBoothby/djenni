package com.robertboothby.djenni.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import static com.robertboothby.djenni.core.SupplierHelper.afterGetCalled;

/**
 * Little utility to make it easier to capture and reuse the results of previous rounds of generation. This is intended
 * to supplement the LinkableSupplier capability while creating more complex domain models.
 * @param <T> The type of the values to be stored.
 */
public class ValueStore<T> {

    private final Set<T> values = new HashSet<>();

    public ValueStore(){}

    public ValueStore(Collection<? extends T> initialValues){
        values.addAll(initialValues);
    }

    @SafeVarargs
    public ValueStore(T ... initialValues){
        Collections.addAll(values, initialValues);
    }

    /**
     * Create a supplier that decorates the naked supplier to store the values that it supplies. You need to use the
     * newly created supplier to get the values and store them. The naked supplier if used on its own will just create
     * the values and not store them.
     * @param valueSupplier The Supplier that will generate the values to be stored.
     * @return a Supplier that uses the passed in supplier to generate the values that will then store them in this
     * ValueStore.
     */
    public StreamableSupplier<T> storesValuesFromSupplier(Supplier<? extends T> valueSupplier){
        return afterGetCalled(valueSupplier, this::storeValue);
    }

    private void storeValue(T value){
        values.add(value);
    }

    /**
     * Create a Supplier that will randomly draw on the values held within this store. The Supplier created will
     * only draw on the values held at the time that this method is called. If new values are added then a new
     * Supplier will need to be created to add them.
     * @return A Supplier that will draw on the values held within this store.
     */
    public StreamableSupplier<T> supplierOfValues(){
        return SupplierHelper.fromCollection(values);
    }

    /**
     * Initiate an instance of ValueStore that stores values of a particular type.
     * @param clazz The class of the type to be stored.
     * @param <T> The type of the values to be stored.
     * @return An empty value store;
     */
    public static <T> ValueStore<T> valueStore(Class<? extends T> clazz){
        return new ValueStore<>();
    }

    /**
     * Initiate an instance of ValueStore that stores values of a particular type, initialised from an initial set of
     * values.
     * @param initialValues The initial values to use.
     * @param <T> The type of the values to be stored.
     * @return A ValueStore with an initial set of values.
     */
    public static <T> ValueStore<T> valueStore(Collection<? extends T> initialValues){
        return new ValueStore<>(initialValues);
    }

    /**
     * Initiate an instance of ValueStore that stores values of a particular type, initialised from an initial set of
     * values.
     * @param initialValues The initial values to use.
     * @param <T> The type of the values to be stored.
     * @return A ValueStore with an initial set of values.
     */
    @SafeVarargs
    public static <T> ValueStore<T> valueStore(T ... initialValues){
        return new ValueStore<>(initialValues);
    }
}
