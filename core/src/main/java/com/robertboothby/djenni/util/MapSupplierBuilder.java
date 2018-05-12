package com.robertboothby.djenni.util;

import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;

import java.util.Map;
import java.util.function.Supplier;

public class MapSupplierBuilder<M extends Map<? extends K, ? extends V>, K, V> implements SupplierBuilder<M> {

    private final MapType<M, K, V> mapType;
    private Supplier<? extends M> contentSupplier;

    private MapSupplierBuilder(MapType<M, K, V> mapType) {
        this.mapType = mapType;
    }

    @Override
    public StreamableSupplier<M> build() {
        return () -> mapType.instance(contentSupplier.get());
    }

    public MapSupplierBuilder<M, K, V> withContentSupplier(Supplier<? extends M> contentSupplier) {
        this.contentSupplier = contentSupplier;
        return this;
    }

    public MapSupplierBuilder<M, K, V> withContentSupplier(SupplierBuilder<? extends M> contentSupplierBuilder) {
        this.contentSupplier = contentSupplierBuilder.build();
        return this;
    }



}
