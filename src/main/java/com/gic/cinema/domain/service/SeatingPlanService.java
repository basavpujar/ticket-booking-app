package com.gic.cinema.domain.service;

import com.gic.cinema.domain.interfaces.SeatingPlan;

public class SeatingPlanService implements SeatingPlan {
    private final char[][] seatingPlan;
    private int availableSeats;

    public SeatingPlanService(int rows, int seatsPerRow) {
        this.seatingPlan = new char[rows][seatsPerRow];
        this.availableSeats = 0;
    }

    public void initializeSeating(int rows, int seatsPerRow) {
        availableSeats = seatingPlan.length * seatingPlan[0].length;
        for (char[] row : seatingPlan) {
            for (int i = 0; i < row.length; i++) {
                row[i] = '.';
            }
        }
    }

    public char[][] getSeatingPlan() {
        return seatingPlan;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public boolean areSeatsAvailable(int numberOfTickets) {
        return availableSeats >= numberOfTickets;
    }

    public void updateAvailableSeats() {
        int available = 0;
        for (char[] row : seatingPlan) {
            for (char seat : row) {
                if (seat == '.') {
                    available++;
                }
            }
        }
        this.availableSeats = available;
    }

}
