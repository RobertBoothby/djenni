package com.robertboothby.djenni.core;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class to manage large numbers of caching suppliers.
 *
 * It is intended to allow you to register caching suppliers in named groups that can be ticked over into new values on demand.
 *
 * Currently runs next() in series due to needing to support {@link ThreadLocalSupplier}s, we may be able to improve
 * this behaviour with the introduction of ScopedValues in the future and deprecating ThreadLocalSupplier.
 */
public class CachingSupplierRegistry {

    /**
     * The default group that supplier will be added to if no group is defined.
     */
    public static final String DEFAULT = "";

    private final Map<String, Set<CachingSupplier<?>>> registeredSuppliers = new ConcurrentHashMap<>();

    /**
     * Create a new empty registry. Each registry maintains its own set of named groups.
     */
    public static CachingSupplierRegistry registry() {
        return new CachingSupplierRegistry();
    }

    /**
     * Add a Caching supplier to a particular group.
     * @param group The group to add the CachingSupplier to.
     * @param supplier The caching supplier to add to the group.
     * @return The registry for further calls.
     */
    public CachingSupplierRegistry addCachingSupplier(String group, CachingSupplier<?> supplier){
        registeredSuppliers.computeIfAbsent(group, k -> ConcurrentHashMap.newKeySet()).add(supplier);
        return this;
    }

    /**
     * Add a CachingSupplier to the default group.
     * @param supplier The supplier to add to the DEFAULT group.
     * @return The registry for further calls.
     */
    public CachingSupplierRegistry addCachingSupplier(CachingSupplier<?> supplier){
        return addCachingSupplier(DEFAULT, supplier);
    }

    /**
     * Calls next() on all CachingSuppliers in a given group.
     * @param group The group of CachingSuppliers to call next() on.
     * @return The registry for further calls.
     */
    public CachingSupplierRegistry next(String group){
        Set<CachingSupplier<?>> suppliers = registeredSuppliers.get(group);
        if (suppliers == null || suppliers.isEmpty()) {
            return this;
        }
        suppliers.forEach(CachingSupplier::next);
        return this;
    }

    /**
     * Call next() on all CachingSuppliers in the DEFAULT group.
     * @return the registry for further calls.
     */
    public CachingSupplierRegistry next(){
        return next(DEFAULT);
    }

    /**
     * Calls next() on <em>all</em> CachingSuppliers that have been registered regardless of group.
     * Invocation order is undefined but currently sequential to maintain compatibility with {@link ThreadLocalSupplier}.
     */
    public void nextAll(){
        registeredSuppliers.values().stream().flatMap(Collection::stream).forEach(CachingSupplier::next);
    }

}
