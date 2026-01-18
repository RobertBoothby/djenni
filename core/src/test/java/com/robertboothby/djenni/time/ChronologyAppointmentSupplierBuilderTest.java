package com.robertboothby.djenni.time;

import com.robertboothby.djenni.core.StreamableSupplier;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.chrono.ThaiBuddhistChronology;
import java.time.chrono.ThaiBuddhistDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ChronologyAppointmentSupplierBuilderTest {

    @Test
    public void shouldCreateThaiBuddhistAppointment() {
        StreamableSupplier<ChronologyAppointmentSupplierBuilder.ChronoAppointment<ThaiBuddhistDate>> supplier =
                ChronologyAppointmentSupplierBuilder.appointmentFor(ChronologyLocalDateSupplierBuilder.thaiBuddhistDate()
                                .between(ThaiBuddhistDate.of(2567, 1, 1))
                                .and(ThaiBuddhistDate.of(2567, 1, 2)))
                        .timeSupplier(() -> LocalTime.of(9, 30))
                        .duration(() -> Duration.ofMinutes(45))
                        .withZone(ZoneId.of("Asia/Bangkok"))
                        .build();

        ChronologyAppointmentSupplierBuilder.ChronoAppointment<ThaiBuddhistDate> appointment = supplier.get();
        assertThat(appointment.getStart(), is(ThaiBuddhistDate.of(2567, 1, 1).atTime(LocalTime.of(9, 30))));
        assertThat(appointment.getDuration(), is(Duration.ofMinutes(45)));
    }

    @Test
    public void shouldCreateIntervalFromAppointment() {
        StreamableSupplier<ChronologyAppointmentSupplierBuilder.ChronoInterval<ThaiBuddhistDate>> intervals =
                ChronologyAppointmentSupplierBuilder.appointmentFor(ChronologyLocalDateSupplierBuilder.thaiBuddhistDate()
                                .between(ThaiBuddhistDate.of(2567, 1, 1))
                                .and(ThaiBuddhistDate.of(2567, 1, 3)))
                        .timeSupplier(() -> LocalTime.MIDNIGHT)
                        .duration(() -> Duration.ofDays(1))
                        .buildIntervals();

        ChronologyAppointmentSupplierBuilder.ChronoInterval<ThaiBuddhistDate> interval = intervals.get();
        assertThat(interval.getEnd(), is(interval.getStart().plus(Duration.ofDays(1))));
    }
}
