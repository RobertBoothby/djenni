package com.robertboothby.djenni.time;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.jupiter.api.Test;
import org.threeten.extra.MutableClock;
import org.threeten.extra.scale.TaiInstant;
import org.threeten.extra.scale.TimeSource;
import org.threeten.extra.scale.UtcInstant;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ClockSupplierBuilderTest {

    @Test
    public void shouldBuildSystemUTCClock() {
        StreamableSupplier<Clock> supplier = ClockSupplierBuilder.aClock().systemUTC().build();
        assertThat(supplier.get().getZone(), is(ZoneOffset.UTC));
    }

    @Test
    public void shouldBuildFixedClock() {
        Instant instant = Instant.ofEpochSecond(1_000);
        StreamableSupplier<Clock> supplier = ClockSupplierBuilder.aClock()
                .fixed(instant, ZoneOffset.UTC)
                .build();

        assertThat(supplier.get().instant(), is(instant));
    }

    @Test
    public void shouldApplyOffset() {
        Instant instant = Instant.ofEpochSecond(500);
        StreamableSupplier<Clock> supplier = ClockSupplierBuilder.aClock()
                .supplier(() -> Clock.fixed(instant, ZoneOffset.UTC))
                .offset(Duration.ofSeconds(10))
                .build();

        assertThat(supplier.get().instant(), is(instant.plusSeconds(10)));
    }

    @Test
    public void shouldApplyTick() {
        StreamableSupplier<Clock> supplier = ClockSupplierBuilder.aClock()
                .supplier(() -> Clock.fixed(Instant.parse("2024-01-01T00:00:00.750Z"), ZoneOffset.UTC))
                .tick(Duration.ofSeconds(1))
                .build();

        assertThat(supplier.get().instant(), is(Instant.parse("2024-01-01T00:00:00Z")));
    }

    @Test
    public void shouldSwitchZone() {
        ZoneId zone = ZoneId.of("Europe/Paris");
        StreamableSupplier<Clock> supplier = ClockSupplierBuilder.aClock()
                .systemUTC()
                .withZone(zone)
                .build();

        assertThat(supplier.get().getZone(), is(zone));
    }

    @Test
    public void shouldExposeMutableClockHandleAndAdvancePerGet() {
        Instant start = Instant.parse("2024-03-01T10:15:30Z");
        ClockSupplierBuilder builder = ClockSupplierBuilder.aClock()
                .mutable(start, ZoneOffset.UTC)
                .advanceOnEachGet(Duration.ofSeconds(30));

        ClockSupplierBuilder.ControlledClockSupplier built = builder.buildControllable();
        MutableClock handle = built.mutableClock().orElseThrow(AssertionError::new);
        StreamableSupplier<Clock> supplier = built.supplier();

        assertThat(supplier.get().instant(), is(start.plusSeconds(30)));

        handle.add(Duration.ofSeconds(30));
        assertThat(supplier.get().instant(), is(start.plusSeconds(90)));
    }

    @Test
    public void shouldApplyAdjusterOnEachGet() {
        TemporalAdjuster plusHourAdjuster = temporal -> temporal.plus(1, ChronoUnit.HOURS);
        Instant start = Instant.parse("2024-05-05T00:00:00Z");

        StreamableSupplier<Clock> supplier = ClockSupplierBuilder.aClock()
                .mutable(start, ZoneOffset.UTC)
                .applyAdjusterOnEachGet(plusHourAdjuster)
                .build();

        assertThat(supplier.get().instant(), is(start.plus(1, ChronoUnit.HOURS)));
        assertThat(supplier.get().instant(), is(start.plus(2, ChronoUnit.HOURS)));
    }

    @Test
    public void shouldProvideMultiZoneViewsForMutableClock() {
        Instant start = Instant.parse("2024-06-01T12:00:00Z");
        ClockSupplierBuilder builder = ClockSupplierBuilder.aClock().mutable(start, ZoneOffset.UTC);

        Map<ZoneId, Clock> views = builder.multiZoneViews(ZoneOffset.UTC, ZoneId.of("Europe/Paris"));

        assertThat(views.get(ZoneOffset.UTC).getZone(), is(ZoneOffset.UTC));
        assertThat(views.get(ZoneId.of("Europe/Paris")).getZone(), is(ZoneId.of("Europe/Paris")));

        builder.mutableClockHandle().ifPresent(clock -> clock.add(Duration.ofHours(1)));

        assertThat(views.get(ZoneOffset.UTC).instant(), is(start.plus(1, ChronoUnit.HOURS)));
    }

    @Test
    public void shouldBuildFromTimeSource() {
        Instant instant = Instant.parse("2025-01-01T00:00:00Z");
        UtcInstant utcInstant = UtcInstant.of(instant);
        TaiInstant taiInstant = utcInstant.toTaiInstant();

        TimeSource stubSource = new TimeSource() {
            @Override
            public Instant instant() {
                return instant;
            }

            @Override
            public UtcInstant utcInstant() {
                return utcInstant;
            }

            @Override
            public TaiInstant taiInstant() {
                return taiInstant;
            }
        };

        StreamableSupplier<Clock> supplier = ClockSupplierBuilder.aClock()
                .timeSource(stubSource, ZoneOffset.ofHours(-5))
                .build();

        Clock clock = supplier.get();
        assertThat(clock.instant(), is(instant));
        assertThat(clock.getZone(), is(ZoneOffset.ofHours(-5)));
    }

    @Test
    public void builtSuppliersShouldRemainUnaffectedByFurtherBuilderActions() {
        Instant start = Instant.parse("2024-07-01T00:00:00Z");
        ClockSupplierBuilder builder = ClockSupplierBuilder.aClock()
                .mutable(start, ZoneOffset.UTC)
                .advanceOnEachGet(Duration.ofMinutes(5));

        StreamableSupplier<Clock> originalSupplier = builder.build();

        assertThat(originalSupplier.get().instant(), is(start.plus(5, ChronoUnit.MINUTES)));

        TemporalAdjuster jumpDay = temporal -> temporal.plus(1, ChronoUnit.DAYS);
        builder.applyAdjusterOnEachGet(jumpDay);

        assertThat(originalSupplier.get().instant(), is(start.plus(10, ChronoUnit.MINUTES)));
    }
}
