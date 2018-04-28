package com.robertboothby.djenni.lang;

import com.robertboothby.djenni.SupplierBuilder;
import org.apache.commons.lang3.NotImplementedException;

import java.util.function.Supplier;

public class ShortSupplierBuilder implements SupplierBuilder<Short> {
    @Override
    public Supplier<Short> build() {
        throw new NotImplementedException("Implement me!");
    }
}
