package com.robertboothby.djenni.util;

import java.util.Collection;
import java.util.List;

/**
 * This interface defines the contract for a set of helpers that instantiate Collections from a list of values.
 * <p>
 * The CollectionType interface's primary purpose is to make it easy to create a general CollectionSupplierBuilder that
 * handles all core Java collection types and is easily extensible to cover other collection types as well.
 * <p>
 * Maps do not implement the {@link Collection} interface and are treated separately in the {@link MapType} interface.
 * @param <T> The type of the Collection being represented.
 * @param <U> The type of the values contained in the collection.
 * @todo Add static methods for the List, Set, SortedSet etc. interfaces.
 */
public interface CollectionType<T extends Collection<U>, U> {

    /**
     * Implementations of this interface will instantiate a the relevant collection type based on the values in the
     * passed in list. The ordering of the values in the list may or may not have an impact on the result depending
     * on the underlying list type.
     * @param values The values to instantiate the list with.
     * @return A newly instantiated collection containing the values passed in.
     */
    T instance(List<U> values);

}
