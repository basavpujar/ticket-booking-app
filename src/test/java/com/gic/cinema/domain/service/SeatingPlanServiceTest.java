package com.gic.cinema.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SeatingPlanServiceTest {

    private SeatingPlanService seatingPlanService;

    @BeforeEach
    void setup() {
        seatingPlanService = new SeatingPlanService(5, 5);
        seatingPlanService.initializeSeating(5, 5);
    }

    @Test
    void testInitializeSeating() {
        char[][] seating = seatingPlanService.getSeatingPlan();

        assertEquals(5, seating.length);
        assertEquals(5, seating[0].length);

        for (char[] row : seating) {
            for (char seat : row) {
                assertEquals('.', seat);
            }
        }
    }

    @Test
    void testGetAvailableSeats_initialState() {
        assertEquals(25, seatingPlanService.getAvailableSeats());
    }

    @Test
    void testAreSeatsAvailable_withEnoughSeats() {
        assertTrue(seatingPlanService.areSeatsAvailable(10));
    }

    @Test
    void testAreSeatsAvailable_withNotEnoughSeats() {
        assertFalse(seatingPlanService.areSeatsAvailable(30));
    }

    @Test
    void testUpdateAvailableSeats_afterBooking() {
        char[][] seating = seatingPlanService.getSeatingPlan();
        seating[0][0] = '#';
        seating[0][1] = '#';
        seating[0][2] = '#';
        seating[0][3] = '#';
        seating[0][4] = '#';

        seatingPlanService.updateAvailableSeats();

        assertEquals(20, seatingPlanService.getAvailableSeats());
    }

    @Test
    void testUpdateAvailableSeats_afterClearingSeats() {
        char[][] seating = seatingPlanService.getSeatingPlan();
        seating[0][0] = '#';
        seating[0][1] = '#';
        seating[0][2] = '#';
        seating[0][3] = '#';
        seating[0][4] = '#';

        seatingPlanService.updateAvailableSeats();

        seating[0][0] = '.';
        seating[0][1] = '.';
        seating[0][2] = '.';
        seating[0][3] = '.';
        seating[0][4] = '.';

        seatingPlanService.updateAvailableSeats();
        assertEquals(25, seatingPlanService.getAvailableSeats());
    }
}

