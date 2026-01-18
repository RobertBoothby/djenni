package com.robertboothby.djenni.time;

import java.time.chrono.Chronology;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Utility that exposes all {@link Chronology} implementations visible on the classpath. This includes the standard
 * JDK chronologies (ISO, Thai Buddhist, Japanese, etc.) and any additional ones contributed through the Java service
 * loader mechanism (for example, the chronologies bundled with ThreeTen-Extra).
 */
final class ChronologyCatalog {

    private static final Map<String, Chronology> DISPLAY_IDS;
    private static final Map<String, Chronology> LOOKUP;

    static {
        Map<String, Chronology> displayIds = new LinkedHashMap<>();
        Map<String, Chronology> lookup = new LinkedHashMap<>();
        for (Chronology chronology : Chronology.getAvailableChronologies()) {
            displayIds.putIfAbsent(chronology.getId(), chronology);
            lookup.putIfAbsent(normalise(chronology.getId()), chronology);
            if (chronology.getCalendarType() != null) {
                lookup.putIfAbsent(normalise(chronology.getCalendarType()), chronology);
            }
        }
        DISPLAY_IDS = Collections.unmodifiableMap(displayIds);
        LOOKUP = Collections.unmodifiableMap(lookup);
    }

    private ChronologyCatalog() {
    }

    static Set<String> availableChronologyIds() {
        return DISPLAY_IDS.keySet();
    }

    static Optional<Chronology> findChronology(String idOrCalendarType) {
        Objects.requireNonNull(idOrCalendarType, "idOrCalendarType");
        String normalised = normalise(idOrCalendarType);
        return Optional.ofNullable(LOOKUP.get(normalised));
    }

    static Chronology requireChronology(String idOrCalendarType) {
        return findChronology(idOrCalendarType)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No chronology found for identifier '" + idOrCalendarType + "'. "
                                + "Available identifiers: " + DISPLAY_IDS.keySet()));
    }

    private static String normalise(String value) {
        return value.toLowerCase(Locale.ROOT);
    }
}
