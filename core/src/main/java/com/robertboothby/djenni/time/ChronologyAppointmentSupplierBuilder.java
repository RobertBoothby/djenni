package com.robertboothby.djenni.time;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.Chronology;
import java.util.Objects;
import java.util.function.Supplier;

import static com.robertboothby.djenni.time.ChronologyLocalDateSupplierBuilder.chronologyDate;

/**
 * Composes a chronology-aware date supplier with a time-of-day and optional duration so callers can generate
 * appointments for non-ISO calendars without boilerplate.
 */
public class ChronologyAppointmentSupplierBuilder<C extends ChronoLocalDate>
        implements ConfigurableSupplierBuilder<ChronologyAppointmentSupplierBuilder.ChronoAppointment<C>, ChronologyAppointmentSupplierBuilder<C>> {

    private final ChronologyLocalDateSupplierBuilder<C> dateBuilder;
    private Supplier<LocalTime> timeSupplier = () -> LocalTime.NOON;
    private Supplier<Duration> durationSupplier = () -> Duration.ZERO;
    private ZoneId zone = ZoneId.systemDefault();

    private ChronologyAppointmentSupplierBuilder(ChronologyLocalDateSupplierBuilder<C> dateBuilder) {
        this.dateBuilder = dateBuilder;
    }

    public static <C extends ChronoLocalDate> ChronologyAppointmentSupplierBuilder<C> appointmentFor(ChronologyLocalDateSupplierBuilder<C> dateBuilder) {
        return new ChronologyAppointmentSupplierBuilder<>(dateBuilder);
    }

    public static ChronologyAppointmentSupplierBuilder<ChronoLocalDate> appointmentFor(String chronologyId) {
        return appointmentFor(chronologyDate(ChronologyCatalog.requireChronology(chronologyId)));
    }

    public ChronologyAppointmentSupplierBuilder<C> between(C startInclusive, C endExclusive) {
        this.dateBuilder.between(startInclusive).and(endExclusive);
        return this;
    }

    public ChronologyAppointmentSupplierBuilder<C> timeSupplier(Supplier<LocalTime> supplier) {
        this.timeSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    public ChronologyAppointmentSupplierBuilder<C> duration(Supplier<Duration> durationSupplier) {
        this.durationSupplier = Objects.requireNonNull(durationSupplier, "durationSupplier");
        return this;
    }

    public ChronologyAppointmentSupplierBuilder<C> withZone(ZoneId zone) {
        this.zone = Objects.requireNonNull(zone, "zone");
        return this;
    }

    @Override
    public StreamableSupplier<ChronoAppointment<C>> build() {
        StreamableSupplier<C> dates = dateBuilder.build();
        Supplier<LocalTime> times = this.timeSupplier;
        Supplier<Duration> durations = this.durationSupplier;
        ZoneId zone = this.zone;
        return () -> {
            C date = dates.get();
            @SuppressWarnings("unchecked")
            ChronoLocalDateTime<C> start = (ChronoLocalDateTime<C>) date.atTime(times.get());
            return new ChronoAppointment<>(start, durations.get(), zone);
        };
    }

    public StreamableSupplier<ChronoInterval<C>> buildIntervals() {
        StreamableSupplier<ChronoAppointment<C>> appointments = build();
        return appointments.derive(ChronoAppointment::toInterval);
    }

    public static final class ChronoAppointment<C extends ChronoLocalDate> {
        private final ChronoLocalDateTime<C> start;
        private final Duration duration;
        private final ZoneId zone;

        private ChronoAppointment(ChronoLocalDateTime<C> start, Duration duration, ZoneId zone) {
            this.start = start;
            this.duration = duration;
            this.zone = zone;
        }

        public ChronoLocalDateTime<C> getStart() {
            return start;
        }

        public Duration getDuration() {
            return duration;
        }

        public ZoneId getZone() {
            return zone;
        }

        public ChronoInterval<C> toInterval() {
            ChronoLocalDateTime<C> end = duration.isZero() ? start : start.plus(duration);
            return new ChronoInterval<>(start, end, zone);
        }
    }

    public static final class ChronoInterval<C extends ChronoLocalDate> {
        private final ChronoLocalDateTime<C> start;
        private final ChronoLocalDateTime<C> end;
        private final ZoneId zone;

        private ChronoInterval(ChronoLocalDateTime<C> start, ChronoLocalDateTime<C> end, ZoneId zone) {
            this.start = start;
            this.end = end;
            this.zone = zone;
        }

        public ChronoLocalDateTime<C> getStart() {
            return start;
        }

        public ChronoLocalDateTime<C> getEnd() {
            return end;
        }

        public ZoneId getZone() {
            return zone;
        }
    }
}
