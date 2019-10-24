package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import org.apache.commons.lang3.NotImplementedException;

public class ShortSupplierBuilder implements ConfigurableSupplierBuilder<Short, ShortSupplierBuilder> {
    @Override
    public StreamableSupplier<Short> build() {
        throw new NotImplementedException("Implement me!");
    }
}
