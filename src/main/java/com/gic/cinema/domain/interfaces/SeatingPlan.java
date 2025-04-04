package com.gic.cinema.domain.interfaces;

public interface SeatingPlan {
    void initializeSeating(int rows, int columns);

    char[][] getSeatingPlan();

    void updateAvailableSeats();

    int getAvailableSeats();

    boolean areSeatsAvailable(int numberOfTickets);
}
