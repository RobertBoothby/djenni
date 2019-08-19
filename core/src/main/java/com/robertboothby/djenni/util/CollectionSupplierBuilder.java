package com.robertboothby.djenni.util;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Builder for all supplier types based on {@link Collection}. It relies on instances of the {@link CollectionType} interface to instantiate
 * the collections being built.
 * <p>
 * There are static methods for most types of Collection on the CollectionType interface or its descendents covering most
 * if not all the collections shipped with the JVM.
 * @param <T> The type of the Collection.
 * @param <U> The type of the values to be held in the collection.
 */
public class CollectionSupplierBuilder<T extends Collection<? extends U>, U> implements ConfigurableSupplierBuilder<T, CollectionSupplierBuilder<T, U>> {

    private final CollectionType<T, U> collectionType;
    private Supplier<? extends List<? extends U>> contentSupplier;

    /**
     * Create an instance of the builder for the given collection type.
     * @param collectionType the type of collection to build.
     */
    private CollectionSupplierBuilder(CollectionType<T, U> collectionType) {
        this.collectionType = collectionType;
    }

    @Override
    public StreamableSupplier<T> build() {
        return () -> collectionType.instance(contentSupplier.get());
    }

    /**
     * Set the supplier for the content.
     * @param contentSupplier the content supplier.
     * @return The builder for further configuration.
     */
    public CollectionSupplierBuilder<T, U> withContent(Supplier<? extends List<? extends U>> contentSupplier) {
        this.contentSupplier = contentSupplier;
        return this;
    }

    /**
     * Set the supplier for the content using a builder.
     * @param contentSupplierBuilder a content supplier builder that will immediately be built.
     * @return The builder for further configuration.
     */
    public CollectionSupplierBuilder<T, U> withContent(SupplierBuilder<? extends List<? extends U>> contentSupplierBuilder) {
        this.contentSupplier = contentSupplierBuilder.build();
        return this;
    }

    /**
     * Get an instance of the builder configured to create instances of a particular Collection type.
     * <p>
     * This method will need extra Generic typing information
     * (<pre>CollectionSupplierBuilder.&lt;LinkedList&lt;String&gt;, String&gt;collection(linkedList())</pre>)
     * to be used successfully.
     * @param collectionType The type of Collection to build.
     * @param <T> The type of the collection.
     * @param <U> The type of the values to be held in the collection.
     * @return a configured collection builder.
     */
    public static <T extends Collection<? extends U>, U> CollectionSupplierBuilder<T, U> collection(CollectionType<T, U> collectionType) {
        return new CollectionSupplierBuilder<>(collectionType);
    }

    /**
     * Get an instance of the builder configured to create instances of a particular Collection type.
     * <p>
     * By taking the class as a parameter this method reduces the generic typing boiler plate.
     * @param collectionType The type of Collection to build.
     * @param tClass The class of the value to be held in the collection.
     * @param <T> The type of the collection.
     * @param <U> The type of the values to be held in the collection.
     * @return a configured collection builder.
     */
    public static <T extends Collection<U>, U> CollectionSupplierBuilder<T, U> collection(CollectionType<T, U> collectionType, Class<U> tClass) {
        return new CollectionSupplierBuilder<>(collectionType);
    }

    /**
     * Get an instance of the builder configured to create instances of a particular collection type.
     * @param collectionType The type of Collection to build.
     * @param valueSupplier the value supplier to use.
     * @param <T> The type of the collection.
     * @param <U> The type of the values to be held in the collection.
     * @return a configured collection builder.
     */
    public static <T extends Collection<U>, U> CollectionSupplierBuilder<T, U> collection(CollectionType<T, U> collectionType, Supplier<U> valueSupplier) {
        return new CollectionSupplierBuilder<>(collectionType);
    }

    /**
     * Create a Supplier using the passed in configuration.
     * @param collectionType The type of Collection to supply.
     * @param configuration The configuration to use.
     * @param <T> The generic type of Collections supplied.
     * @param <U> The generic type of the values contained in the Collections.
     * @return The configured supplier.
     */
    public static <T extends Collection<U>, U> Supplier<T> collection(CollectionType<T,U> collectionType, Consumer<CollectionSupplierBuilder<T,U>> configuration){
        CollectionSupplierBuilder<T, U> builder = collection(collectionType);
        configuration.accept(builder);
        return builder.build();
    }
}
