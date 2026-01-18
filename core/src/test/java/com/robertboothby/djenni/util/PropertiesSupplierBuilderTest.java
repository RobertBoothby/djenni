package com.robertboothby.djenni.util;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.jupiter.api.Test;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import static com.robertboothby.djenni.core.SupplierHelper.fix;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class PropertiesSupplierBuilderTest {

    @Test
    public void shouldGenerateRandomProperties() {
        AtomicInteger counter = new AtomicInteger();
        StreamableSupplier<Properties> supplier = PropertiesSupplierBuilder.propertiesSupplier()
                .randomEntryCount(fix(3))
                .randomKeySupplier(() -> "key-" + counter.incrementAndGet())
                .randomValueSupplier(() -> "value-" + counter.get())
                .build();

        Properties properties = supplier.get();
        assertThat(properties.size(), is(3));
        assertThat(properties.getProperty("key-1"), is("value-1"));
    }

    @Test
    public void shouldAlwaysIncludeRequiredProperties() {
        StreamableSupplier<Properties> supplier = PropertiesSupplierBuilder.propertiesSupplier()
                .includeRandomEntries(false)
                .withRequiredProperty("env", fix("prod"))
                .build();

        Properties props = supplier.get();
        assertThat(props.getProperty("env"), is("prod"));
    }

    @Test
    public void shouldIncludeOptionalPropertyWhenProbabilityOne() {
        StreamableSupplier<Properties> supplier = PropertiesSupplierBuilder.propertiesSupplier()
                .includeRandomEntries(false)
                .withOptionalProperty("feature", fix("enabled"), 1.0D)
                .build();

        assertThat(supplier.get().getProperty("feature"), is("enabled"));
    }

    @Test
    public void shouldExcludeOptionalPropertyWhenProbabilityZero() {
        StreamableSupplier<Properties> supplier = PropertiesSupplierBuilder.propertiesSupplier()
                .includeRandomEntries(false)
                .withOptionalProperty("feature", fix("enabled"), 0.0D)
                .build();

        assertThat(supplier.get().getProperty("feature"), is(nullValue()));
    }

    @Test
    public void builtPropertiesSuppliersShouldRemainStableAfterBuilderChanges() {
        PropertiesSupplierBuilder builder = PropertiesSupplierBuilder.propertiesSupplier()
                .includeRandomEntries(false)
                .withRequiredProperty("env", fix("dev"));

        StreamableSupplier<Properties> supplier = builder.build();

        builder.withRequiredProperty("env", fix("prod"));

        assertThat(supplier.get().getProperty("env"), is("dev"));
    }
}
