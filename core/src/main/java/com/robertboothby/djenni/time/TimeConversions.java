package com.robertboothby.djenni.time;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * Utility methods for converting the array of JVM date/time representations into {@link Instant} instances.
 */
final class TimeConversions {

    private TimeConversions() {
    }

    static Instant toInstant(Instant instant) {
        return Objects.requireNonNull(instant, "instant");
    }

    static Instant toInstant(ZonedDateTime zonedDateTime) {
        return Objects.requireNonNull(zonedDateTime, "zonedDateTime").toInstant();
    }

    static Instant toInstant(OffsetDateTime offsetDateTime) {
        return Objects.requireNonNull(offsetDateTime, "offsetDateTime").toInstant();
    }

    static Instant toInstant(LocalDateTime localDateTime, ZoneId zone) {
        return Objects.requireNonNull(localDateTime, "localDateTime").atZone(zone).toInstant();
    }

    static Instant toInstant(LocalDate localDate, ZoneId zone) {
        return Objects.requireNonNull(localDate, "localDate").atStartOfDay(zone).toInstant();
    }

    static Instant toInstant(Date date) {
        return Instant.ofEpochMilli(Objects.requireNonNull(date, "date").getTime());
    }

    static Instant toInstant(java.sql.Date date) {
        return Instant.ofEpochMilli(Objects.requireNonNull(date, "date").getTime());
    }

    static Instant toInstant(Time time) {
        return Instant.ofEpochMilli(Objects.requireNonNull(time, "time").getTime());
    }

    static Instant toInstant(Timestamp timestamp) {
        return Instant.ofEpochMilli(Objects.requireNonNull(timestamp, "timestamp").getTime());
    }

    static Instant toInstant(Calendar calendar) {
        return Instant.ofEpochMilli(Objects.requireNonNull(calendar, "calendar").getTimeInMillis());
    }

    static Instant toInstant(long epochMilli) {
        return Instant.ofEpochMilli(epochMilli);
    }

    static Instant toInstant(Long epochMilli) {
        return toInstant(Objects.requireNonNull(epochMilli, "epochMilli").longValue());
    }
}
