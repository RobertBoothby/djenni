package com.robertboothby.djenni.time;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.core.SupplierHelper;
import org.threeten.extra.MutableClock;
import org.threeten.extra.scale.TimeSource;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjuster;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Builds suppliers of {@link Clock} instances. Uses {@code threeten-extra} clocks to support complex testing scenarios.
 */
public class ClockSupplierBuilder implements ConfigurableSupplierBuilder<Clock, ClockSupplierBuilder> {

    private Supplier<Clock> clockSupplier = Clock::systemUTC;
    private MutableClock activeMutableClock;
    private final List<Consumer<MutableClock>> mutableClockGetActions = new ArrayList<>();

    public static ClockSupplierBuilder aClock() {
        return new ClockSupplierBuilder();
    }

    @Override
    public StreamableSupplier<Clock> build() {
        Supplier<Clock> supplier = this.clockSupplier;
        MutableClock mutableClock = this.activeMutableClock;
        List<Consumer<MutableClock>> onGetActions = this.mutableClockGetActions.isEmpty()
                ? Collections.emptyList()
                : List.copyOf(this.mutableClockGetActions);

        return () -> {
            if (mutableClock != null && !onGetActions.isEmpty()) {
                onGetActions.forEach(action -> action.accept(mutableClock));
            }
            return supplier.get();
        };
    }

    /**
     * Builds the supplier alongside details of the configured {@link MutableClock}, if any.
     */
    public ControlledClockSupplier buildControllable() {
        return new ControlledClockSupplier(build(), Optional.ofNullable(activeMutableClock));
    }

    public ClockSupplierBuilder supplier(Supplier<Clock> supplier) {
        return supplierInternal(supplier, false);
    }

    public ClockSupplierBuilder systemUTC() {
        return supplier(Clock::systemUTC);
    }

    public ClockSupplierBuilder systemDefaultZone() {
        return supplier(Clock::systemDefaultZone);
    }

    public ClockSupplierBuilder fixed(Instant instant, ZoneId zone) {
        Objects.requireNonNull(instant, "instant");
        Objects.requireNonNull(zone, "zone");
        return supplier(() -> Clock.fixed(instant, zone));
    }

    public ClockSupplierBuilder offset(Duration offset) {
        Objects.requireNonNull(offset, "offset");
        return transform(clock -> Clock.offset(clock, offset));
    }

    public ClockSupplierBuilder tick(Duration tickDuration) {
        Objects.requireNonNull(tickDuration, "tickDuration");
        if (tickDuration.isZero() || tickDuration.isNegative()) {
            throw new IllegalArgumentException("Tick duration must be positive.");
        }
        return transform(clock -> Clock.tick(clock, tickDuration));
    }

    public ClockSupplierBuilder withZone(ZoneId zone) {
        Objects.requireNonNull(zone, "zone");
        return transform(clock -> clock.withZone(zone));
    }

    public ClockSupplierBuilder mutableEpochUTC() {
        return mutable(MutableClock.epochUTC());
    }

    public ClockSupplierBuilder mutable(Instant instant, ZoneId zone) {
        Objects.requireNonNull(instant, "instant");
        Objects.requireNonNull(zone, "zone");
        return mutable(MutableClock.of(instant, zone));
    }

    private ClockSupplierBuilder mutable(MutableClock mutableClock) {
        this.activeMutableClock = Objects.requireNonNull(mutableClock, "mutableClock");
        this.mutableClockGetActions.clear();
        return supplierInternal(() -> this.activeMutableClock, true);
    }

    public ClockSupplierBuilder advanceOnEachGet(Duration step) {
        Objects.requireNonNull(step, "step");
        if (step.isZero()) {
            throw new IllegalArgumentException("Advance step must not be zero.");
        }
        ensureMutableClockConfigured("advanceOnEachGet");
        this.mutableClockGetActions.add(clock -> clock.add(step));
        return this;
    }

    public ClockSupplierBuilder applyAdjusterOnEachGet(TemporalAdjuster adjuster) {
        Objects.requireNonNull(adjuster, "adjuster");
        ensureMutableClockConfigured("applyAdjusterOnEachGet");
        this.mutableClockGetActions.add(clock -> clock.set(adjuster));
        return this;
    }

    public Map<ZoneId, Clock> multiZoneViews(ZoneId firstZone, ZoneId... additionalZones) {
        ensureMutableClockConfigured("multiZoneViews");
        Objects.requireNonNull(firstZone, "firstZone");
        Map<ZoneId, Clock> views = new LinkedHashMap<>();
        views.put(firstZone, activeMutableClock.withZone(firstZone));
        if (additionalZones != null) {
            for (ZoneId zone : additionalZones) {
                Objects.requireNonNull(zone, "zone");
                views.put(zone, activeMutableClock.withZone(zone));
            }
        }
        return Collections.unmodifiableMap(views);
    }

    public ClockSupplierBuilder timeSource(TimeSource timeSource) {
        return timeSource(timeSource, ZoneOffset.UTC);
    }

    public ClockSupplierBuilder timeSource(TimeSource timeSource, ZoneId zone) {
        Objects.requireNonNull(timeSource, "timeSource");
        Objects.requireNonNull(zone, "zone");
        return supplier(() -> new TimeSourceClock(timeSource, zone));
    }

    public Optional<MutableClock> mutableClockHandle() {
        return Optional.ofNullable(activeMutableClock);
    }

    private ClockSupplierBuilder transform(Function<Clock, Clock> transformation) {
        Supplier<Clock> base = this.clockSupplier;
        this.clockSupplier = () -> transformation.apply(base.get());
        return this;
    }

    private void ensureMutableClockConfigured(String methodName) {
        if (this.activeMutableClock == null) {
            throw new IllegalStateException(methodName + " requires a configured mutable clock.");
        }
    }

    private ClockSupplierBuilder supplierInternal(Supplier<Clock> supplier, boolean preserveMutableContext) {
        Objects.requireNonNull(supplier, "supplier");
        if (!preserveMutableContext) {
            this.activeMutableClock = null;
            this.mutableClockGetActions.clear();
        }
        this.clockSupplier = SupplierHelper.asStreamable(supplier);
        return this;
    }

    public static final class ControlledClockSupplier {
        private final StreamableSupplier<Clock> supplier;
        private final Optional<MutableClock> mutableClock;

        private ControlledClockSupplier(StreamableSupplier<Clock> supplier, Optional<MutableClock> mutableClock) {
            this.supplier = Objects.requireNonNull(supplier, "supplier");
            this.mutableClock = Objects.requireNonNull(mutableClock, "mutableClock");
        }

        public StreamableSupplier<Clock> supplier() {
            return supplier;
        }

        public Optional<MutableClock> mutableClock() {
            return mutableClock;
        }
    }

    private static final class TimeSourceClock extends Clock {
        private final TimeSource timeSource;
        private final ZoneId zone;

        private TimeSourceClock(TimeSource timeSource, ZoneId zone) {
            this.timeSource = Objects.requireNonNull(timeSource, "timeSource");
            this.zone = Objects.requireNonNull(zone, "zone");
        }

        @Override
        public ZoneId getZone() {
            return zone;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return new TimeSourceClock(timeSource, zone);
        }

        @Override
        public Instant instant() {
            return timeSource.instant();
        }
    }
}
