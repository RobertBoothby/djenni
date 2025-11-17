package com.robertboothby.djenni.time;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.lang.LongSupplierBuilder;
import com.robertboothby.djenni.sugar.And;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.function.Supplier;

public class InstantSupplierBuilder implements ConfigurableSupplierBuilder<Instant, InstantSupplierBuilder> {

    private Supplier<Long> epochMilliSupplier = LongSupplierBuilder.generateALong().build();

    public static InstantSupplierBuilder anInstant() {
        return new InstantSupplierBuilder();
    }

    @Override
    public StreamableSupplier<Instant> build() {
        //dereference the supplier to ensure that it is not changed by further use of the builder.
        Supplier<Long> epochMilliSupplier = this.epochMilliSupplier;
        return () -> Instant.ofEpochMilli(epochMilliSupplier.get());
    }

    public And<InstantSupplierBuilder, Instant> between(Instant start){
        return endExclusive -> {
            epochMilliSupplier = LongSupplierBuilder.generateALong().between(start.toEpochMilli()).and(endExclusive.toEpochMilli()).build();
            return this;
        };
    }

    public And<InstantSupplierBuilder, ZonedDateTime> between(ZonedDateTime start){
        return endExclusive -> {
            epochMilliSupplier = LongSupplierBuilder.generateALong().between(start.toInstant().toEpochMilli()).and(endExclusive.toInstant().toEpochMilli()).build();
            return this;
        };
    }

}
