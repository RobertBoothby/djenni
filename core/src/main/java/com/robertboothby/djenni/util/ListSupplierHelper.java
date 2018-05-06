package com.robertboothby.djenni.util;

import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.core.SupplierHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

public class ListSupplierHelper {

    /**
     * Create a Supplier of Lists whose values are generated by a randomly selected supplier.
     * @param sizes determines the size of the list when it is supplied.
     * @param suppliers the set of suppliers to choose from.
     * @param <T> The type of the values that will be in the supplied Lists.
     * @return A Supplier of the Lists.
     */
    public static <T> StreamableSupplier<List<T>> fromRandomSuppliers(Supplier<Integer> sizes, Supplier<T> ... suppliers){
        StreamableSupplier<T> randomSuppliers = SupplierHelper.fromRandomSuppliers(suppliers);
        return () -> randomSuppliers.stream(sizes.get()).collect(toList());
    }

    /**
     * Create a Supplier of Lists whose values are generated by a randomly selected supplier.
     * @param sizes determines the size of the list when it is supplied.
     * @param positions determines the selection of the suppliers from those passed in allowing for biassing.
     * @param suppliers the set of suppliers to choose from.
     * @param <T> The type of the values that will be in the supplied Lists.
     * @return A Supplier of the Lists.
     */
    public static <T> StreamableSupplier<List<T>> fromRandomSuppliers(Supplier<Integer> sizes, Supplier<Integer> positions, Supplier<T>... suppliers){
        StreamableSupplier<T> randomSuppliers = SupplierHelper.fromRandomSuppliers(positions, suppliers);
        return () -> randomSuppliers.stream(sizes.get()).collect(toList());
    }

    /**
     * Create a Supplier of Lists which will contain a value from each of the Suppliers provided in the order that they
     * are provided.
     * @param suppliers The Suppliers which will contribute values to the Lists being supplied in the order that they are passed in.
     * @param <T> The type of the values that will be in the supplied Lists.
     * @return A Supplier of the Lists.
     */
    public static <T> StreamableSupplier<List<T>> fromSuppliers(Supplier<T> ... suppliers){
        return () -> Arrays.stream(suppliers).map(Supplier::get).collect(toList());
    }

    /**
     * Create a Supplier of Lists that concatenate together the values from the passed in Suppliers of Collections.
     * @param suppliers The suppliers to use.
     * @param <T> The type of the values that will be in the list.
     * @return A Supplier that will produce Lists based on the values supplied by the passed in Suppliers.
     */
    public static <T> StreamableSupplier<List<T>> concatenate(Supplier<? extends Collection<T>> ... suppliers){
        return () -> Arrays.stream(suppliers).map(Supplier::get).collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);

    }
}
