package com.gic.cinema.domain.service;

import com.gic.cinema.domain.model.TicketBooking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class SeatBookingServiceTest {

    private SeatBookingService seatBookingService;

    @BeforeEach
    public void setup() {
        seatBookingService = new SeatBookingService();
        seatBookingService.initializeSeating(5, 5);
    }

    @Test
    public void testInitializeSeating() {
        seatBookingService.initializeSeating(3, 3);
        char[][] seatingPlan = seatBookingService.getSeatingPlan();
        assertEquals(3, seatingPlan.length);
        assertEquals(3, seatingPlan[0].length);
        assertEquals(9, seatBookingService.getAvailableSeats());
    }

    @Test
    public void testAreSeatsAvailable() {
        assertTrue(seatBookingService.areSeatsAvailable(5));
        assertFalse(seatBookingService.areSeatsAvailable(26));
    }


    @Test
    public void testBookSeats() {
        Optional<TicketBooking> booking = seatBookingService.bookSeats(3, "Alien");
        assertTrue(booking.isPresent());
        assertEquals(3, booking.get().ticketsBooked());
        assertEquals("Alien", booking.get().movieTitle());
    }

    @Test
    public void testBookSeats_NotAvailable() {
        seatBookingService.initializeSeating(1, 2);
        Optional<TicketBooking> booking = seatBookingService.bookSeats(3, "Jaws");
        assertFalse(booking.isPresent());
    }

    @Test
    public void testUpdateSeatBooking() {
        TicketBooking initialBooking = seatBookingService.bookSeats(2, "Argo").orElseThrow();
        Optional<TicketBooking> updatedBooking = seatBookingService.updateSeatBooking(3, initialBooking, 0, 0);
        assertTrue(updatedBooking.isPresent());
        assertEquals(3, updatedBooking.get().ticketsBooked());
        assertEquals("Argo", updatedBooking.get().movieTitle());
    }

    @Test
    public void testUpdateSeat_CenterToLeftBooking() {
        TicketBooking initialBooking = seatBookingService.bookSeats(2, "Argo").orElseThrow();
        Optional<TicketBooking> updatedBooking = seatBookingService.updateSeatBooking(8, initialBooking, 0, 0);
        assertTrue(updatedBooking.isPresent());
        assertEquals(8, updatedBooking.get().ticketsBooked());
        assertEquals("Argo", updatedBooking.get().movieTitle());
    }

    @Test
    public void testUpdateSeatBooking_SeatsNotAvailable() {
        seatBookingService.initializeSeating(2, 2);
        TicketBooking initialBooking = seatBookingService.bookSeats(2, "Argo").orElseThrow();
        Optional<TicketBooking> updatedBooking = seatBookingService.updateSeatBooking(3, initialBooking, 0, 0);
        assertEquals(false, updatedBooking.isPresent());
    }

    @Test
    public void testProcessAndAddConfirmedBooking() {
        TicketBooking booking = seatBookingService.bookSeats(2, "Mask").orElseThrow();
        seatBookingService.processAndAddConfirmedBooking(booking);
        Optional<TicketBooking> bookingDetails = seatBookingService.getBookingDetails(booking.bookingId());
        assertTrue(bookingDetails.isPresent());
        assertEquals("Mask", bookingDetails.get().movieTitle());
        assertEquals(1, seatBookingService.getBookingDetailMap().size());
    }

    @Test
    public void testClearPreviousBookingDetails() {
        TicketBooking booking = seatBookingService.bookSeats(2, "Hulk").orElseThrow();
        seatBookingService.processAndAddConfirmedBooking(booking);
        seatBookingService.clearPreviousBookingDetails(booking);
    }

    @Test
    public void testGetBookingDetails() {
        TicketBooking booking = seatBookingService.bookSeats(2, "Inception").orElseThrow();
        seatBookingService.processAndAddConfirmedBooking(booking);
        Optional<TicketBooking> bookingDetails = seatBookingService.getBookingDetails(booking.bookingId());
        assertTrue(bookingDetails.isPresent());
        assertEquals("Inception", bookingDetails.get().movieTitle());
    }

    @Test
    public void testGetBookingDetails_NotFound() {
        Optional<TicketBooking> bookingDetails = seatBookingService.getBookingDetails("NonExistentId");
        assertFalse(bookingDetails.isPresent());
    }
}