package com.robertboothby.djenni.math;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.distribution.Distribution;
import org.apache.commons.lang3.NotImplementedException;
import org.hamcrest.Description;

import java.math.BigDecimal;
import java.util.function.Supplier;

/**
 * FROM STACK OVERFLOW
 * http://stackoverflow.com/questions/2290057/how-to-generate-a-random-biginteger-value-in-java
 *
 * @author robertboothby
 */
public class BigDecimalSupplierBuilder implements ConfigurableSupplierBuilder<BigDecimal, BigDecimalSupplierBuilder> {

    @Override
    public StreamableSupplier<BigDecimal> build() {
        throw new NotImplementedException("Needs implementation.");
    }
}
