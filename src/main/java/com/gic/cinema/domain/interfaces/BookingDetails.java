package com.gic.cinema.domain.interfaces;

import com.gic.cinema.domain.model.TicketBooking;

import java.util.Optional;

public interface BookingDetails {
    Optional<TicketBooking> getBookingDetails(String bookingId);
}
