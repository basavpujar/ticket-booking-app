package com.gic.cinema.domain.interfaces;

import com.gic.cinema.domain.model.TicketBooking;

import java.util.Optional;

public interface BookingManagement {
    Optional<TicketBooking> bookSeats(int numberOfTickets, String movieTitle);

    Optional<TicketBooking> updateSeatBooking(int numberOfTickets, TicketBooking ticketBookingDetails, int startRow, int startColumn);

    void processAndAddConfirmedBooking(TicketBooking ticketBookingDetails);

    void clearPreviousBookingDetails(TicketBooking ticketBookingDetails);
}
