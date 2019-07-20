package com.robertboothby.djenni.math;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import org.apache.commons.lang3.NotImplementedException;

import java.math.BigInteger;

public class BigIntegerSupplierBuilder implements ConfigurableSupplierBuilder<BigInteger, BigIntegerSupplierBuilder> {
    @Override
    public StreamableSupplier<BigInteger> build() {
        throw new NotImplementedException("To be implemented");
    }
}
