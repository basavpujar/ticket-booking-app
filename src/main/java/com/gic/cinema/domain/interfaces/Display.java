package com.gic.cinema.domain.interfaces;

import com.gic.cinema.domain.model.TicketBooking;

public interface Display {
    void println(String message);

    void displayWelcomeScreen(String movieTitle, int seatsAvailable);

    void displaySeatPlan(char[][] seatingPlan, String seatingDetails);

    void displayBookingSummary(int numberOfTickets, char[][] seatingPlan, TicketBooking ticketBookingDetails);

    void printBookingDetails(TicketBooking ticketBookingDetails);
}