package com.robertboothby.djenni.util;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.SupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.core.SupplierHelper;
import com.robertboothby.djenni.distribution.Distribution;
import com.robertboothby.djenni.distribution.simple.SimpleRandomDoubleDistribution;
import com.robertboothby.djenni.lang.StringSupplierBuilder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Supplier;

import static com.robertboothby.djenni.lang.IntegerSupplierBuilder.integerSupplier;

/**
 * Builds suppliers of {@link Properties}. By default the builder generates between one and five entries using random
 * strings for both keys and values. Callers can constrain the output by supplying required and optional keys.
 */
public class PropertiesSupplierBuilder implements ConfigurableSupplierBuilder<Properties, PropertiesSupplierBuilder> {

    private StreamableSupplier<String> randomKeySupplier = StringSupplierBuilder.arbitraryString().build();
    private StreamableSupplier<String> randomValueSupplier = StringSupplierBuilder.arbitraryString().build();
    private Supplier<Integer> randomEntryCountSupplier = integerSupplier(builder -> builder.between(1).and(6));
    private boolean includeRandomEntries = true;

    private final Map<String, Supplier<String>> requiredEntries = new LinkedHashMap<>();
    private final Map<String, OptionalEntry> optionalEntries = new LinkedHashMap<>();
    private Distribution<Double, Double> optionalPresenceDistribution = SimpleRandomDoubleDistribution.UNIFORM;

    public static PropertiesSupplierBuilder propertiesSupplier() {
        return new PropertiesSupplierBuilder();
    }

    @Override
    public StreamableSupplier<Properties> build() {
        StreamableSupplier<String> keySupplier = this.randomKeySupplier;
        StreamableSupplier<String> valueSupplier = this.randomValueSupplier;
        Supplier<Integer> entryCountSupplier = this.randomEntryCountSupplier;
        boolean includeRandom = this.includeRandomEntries;
        Map<String, Supplier<String>> required = new LinkedHashMap<>(this.requiredEntries);
        Map<String, OptionalEntry> optional = new LinkedHashMap<>(this.optionalEntries);
        Distribution<Double, Double> distribution = this.optionalPresenceDistribution;

        return () -> {
            Properties properties = new Properties();
            required.forEach((key, supplier) -> {
                String value = supplier.get();
                if (key != null && value != null) {
                    properties.setProperty(key, value);
                }
            });
            optional.forEach((key, optionalEntry) -> {
                if (distribution.generate(1.0D) < optionalEntry.probability) {
                    String value = optionalEntry.valueSupplier.get();
                    if (key != null && value != null) {
                        properties.setProperty(key, value);
                    }
                }
            });
            if (includeRandom) {
                int desired = Math.max(0, entryCountSupplier.get());
                int guard = desired * 4 + 10;
                while (properties.size() < desired && guard-- > 0) {
                    String key = keySupplier.get();
                    String value = valueSupplier.get();
                    if (key == null || value == null || properties.containsKey(key)) {
                        continue;
                    }
                    properties.setProperty(key, value);
                }
            }
            return properties;
        };
    }

    public PropertiesSupplierBuilder randomKeySupplier(Supplier<String> supplier) {
        this.randomKeySupplier = SupplierHelper.asStreamable(Objects.requireNonNull(supplier, "supplier"));
        return this;
    }

    public PropertiesSupplierBuilder randomValueSupplier(Supplier<String> supplier) {
        this.randomValueSupplier = SupplierHelper.asStreamable(Objects.requireNonNull(supplier, "supplier"));
        return this;
    }

    public PropertiesSupplierBuilder randomEntryCount(Supplier<Integer> supplier) {
        this.randomEntryCountSupplier = SupplierHelper.asStreamable(Objects.requireNonNull(supplier, "supplier"));
        return this;
    }

    public PropertiesSupplierBuilder includeRandomEntries(boolean includeRandomEntries) {
        this.includeRandomEntries = includeRandomEntries;
        return this;
    }

    public PropertiesSupplierBuilder withRequiredProperty(String key, Supplier<String> valueSupplier) {
        requiredEntries.put(key, SupplierHelper.asStreamable(Objects.requireNonNull(valueSupplier, "valueSupplier")));
        return this;
    }

    public PropertiesSupplierBuilder withRequiredProperty(String key, SupplierBuilder<String> valueSupplierBuilder) {
        Objects.requireNonNull(valueSupplierBuilder, "valueSupplierBuilder");
        return withRequiredProperty(key, valueSupplierBuilder.build());
    }

    public PropertiesSupplierBuilder withOptionalProperty(String key, Supplier<String> valueSupplier, double probability) {
        if (probability < 0.0D || probability > 1.0D) {
            throw new IllegalArgumentException("Probability must be between 0.0 and 1.0 inclusive.");
        }
        optionalEntries.put(key, new OptionalEntry(SupplierHelper.asStreamable(Objects.requireNonNull(valueSupplier, "valueSupplier")), probability));
        return this;
    }

    public PropertiesSupplierBuilder withOptionalProperty(String key, SupplierBuilder<String> valueSupplierBuilder, double probability) {
        Objects.requireNonNull(valueSupplierBuilder, "valueSupplierBuilder");
        return withOptionalProperty(key, valueSupplierBuilder.build(), probability);
    }

    public PropertiesSupplierBuilder withOptionalPresenceDistribution(Distribution<Double, Double> distribution) {
        this.optionalPresenceDistribution = Objects.requireNonNull(distribution, "distribution");
        return this;
    }

    private static final class OptionalEntry {
        private final Supplier<String> valueSupplier;
        private final double probability;

        private OptionalEntry(Supplier<String> valueSupplier, double probability) {
            this.valueSupplier = valueSupplier;
            this.probability = probability;
        }
    }
}
