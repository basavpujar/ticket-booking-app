package com.gic.cinema.controller;

import com.gic.cinema.domain.interfaces.BookingService;
import com.gic.cinema.domain.interfaces.Display;
import com.gic.cinema.domain.interfaces.InputHandler;
import com.gic.cinema.domain.model.SeatingConfigurationInput;
import com.gic.cinema.domain.model.TicketBooking;
import com.gic.cinema.domain.util.SeatingConfigurationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CinemaBookingManagerTest {

    @Mock
    private Display cinemaSeatingDisplay;
    @Mock
    private BookingService seatBookingService;
    @Mock
    private InputHandler userInputHandler;
    @Mock
    private SeatingConfigurationValidator seatingConfigurationValidator;

    private CinemaBookingManager cinemaBookingManager;

    @BeforeEach
    void setup() {
        cinemaBookingManager = new CinemaBookingManager(cinemaSeatingDisplay, seatBookingService, userInputHandler, seatingConfigurationValidator);
    }

    @Test
    void testStart_validSeatingInput() {
        String userInput = "Argo 10 10";
        SeatingConfigurationInput seatingConfigurationInput = new SeatingConfigurationInput("Argo", 10, 10);
        when(userInputHandler.getUserAcceptChoice()).thenReturn(userInput).thenReturn("\n");
        when(seatingConfigurationValidator.getSeatingConfigurationFromUser(userInput)).thenReturn(Optional.of(seatingConfigurationInput));
        when(seatBookingService.getAvailableSeats()).thenReturn(100);
        when(userInputHandler.getUserSelection()).thenReturn(3).thenReturn(3);

        cinemaBookingManager.start();

        verify(cinemaSeatingDisplay).println("Please define movie and seating map in [Title] [Row] [SeatsPerRow] format:");
        verify(seatBookingService).initializeSeating(10, 10);
        verify(cinemaSeatingDisplay).displayWelcomeScreen("Argo", 100);
    }

    @Test
    void testStart_invalidSeatingInput() {
        when(userInputHandler.getUserAcceptChoice()).thenReturn("InvalidInput");
        when(seatingConfigurationValidator.getSeatingConfigurationFromUser("InvalidInput")).thenReturn(Optional.empty()).thenReturn(Optional.of(new SeatingConfigurationInput("Argo", 10, 10)));
        when(userInputHandler.getUserSelection()).thenReturn(3).thenReturn(3);

        cinemaBookingManager.start();

        verify(cinemaSeatingDisplay).println("Please define movie and seating map in [Title] [Row] [SeatsPerRow] format:");
        verify(cinemaSeatingDisplay).println("Thank you for using GIC Cinemas system. Bye!");
    }

    @Test
    void testProcessUserSelection_invalidSelection() {
        SeatingConfigurationInput seatingConfigurationInput = new SeatingConfigurationInput("Argo", 10, 10);

        boolean exit = cinemaBookingManager.processUserSelection(-1, seatingConfigurationInput);

        assertFalse(exit);
    }

    @Test
    void testProcessUserSelection_bookTickets() {
        SeatingConfigurationInput seatingConfigurationInput = new SeatingConfigurationInput("Argo", 10, 10);
        when(userInputHandler.getNumberOfTickets()).thenReturn(2);
        when(seatBookingService.getAvailableSeats()).thenReturn(10);
        when(seatBookingService.bookSeats(anyInt(), anyString())).thenReturn(Optional.of(mock(TicketBooking.class)));

        boolean exit = cinemaBookingManager.processUserSelection(1, seatingConfigurationInput);

        assertFalse(exit);
    }

    @Test
    void testProcessUserSelection_checkBooking() {
        SeatingConfigurationInput seatingConfigurationInput = new SeatingConfigurationInput("Argo", 10, 10);

        boolean exit = cinemaBookingManager.processUserSelection(2, seatingConfigurationInput);

        assertFalse(exit);
    }

    @Test
    void testProcessUserSelection_exit() {
        SeatingConfigurationInput seatingConfigurationInput = new SeatingConfigurationInput("Argo", 10, 10);

        boolean exit = cinemaBookingManager.processUserSelection(3, seatingConfigurationInput);

        assertTrue(exit);
        verify(cinemaSeatingDisplay).println("Thank you for using GIC Cinemas system. Bye!");
    }


    @Test
    void testBookTickets_insufficientSeats() {
        SeatingConfigurationInput seatingConfigurationInput = new SeatingConfigurationInput("Argo", 10, 10);
        when(userInputHandler.getNumberOfTickets()).thenReturn(20).thenReturn(-1);
        when(seatBookingService.getAvailableSeats()).thenReturn(10);

        cinemaBookingManager.bookTickets(seatingConfigurationInput);

        verify(cinemaSeatingDisplay).println("Sorry, there are only 10 seats available.");
    }

    @Test
    void testBookTickets_successfulBooking() {
        SeatingConfigurationInput seatingConfigurationInput = new SeatingConfigurationInput("Argo", 10, 10);
        when(userInputHandler.getNumberOfTickets()).thenReturn(2);
        when(seatBookingService.getAvailableSeats()).thenReturn(10);
        when(seatBookingService.bookSeats(2, seatingConfigurationInput.movieTitle())).thenReturn(Optional.of(mock(TicketBooking.class)));

        cinemaBookingManager.bookTickets(seatingConfigurationInput);

        verify(seatBookingService).bookSeats(2, seatingConfigurationInput.movieTitle());
    }

    @Test
    void testCheckBooking_bookingFound() {
        String bookingId = "GIC0001";
        TicketBooking ticketBooking = mock(TicketBooking.class);
        when(userInputHandler.getUserAcceptChoice()).thenReturn(bookingId);
        when(seatBookingService.getBookingDetails(bookingId)).thenReturn(Optional.of(ticketBooking));

        cinemaBookingManager.checkBooking();

        verify(seatBookingService).getBookingDetails(bookingId);
        verify(cinemaSeatingDisplay).printBookingDetails(ticketBooking);
    }

    @Test
    void testCheckBooking_bookingNotFound() {
        String bookingId = "invalidBookingId";
        when(userInputHandler.getUserAcceptChoice()).thenReturn(bookingId);
        when(seatBookingService.getBookingDetails(bookingId)).thenReturn(Optional.empty());

        cinemaBookingManager.checkBooking();

        verify(seatBookingService).getBookingDetails(bookingId);
        verify(cinemaSeatingDisplay).println("Booking Id not found. Please try again!");
    }

    @Test
    void testRebookSeats_invalidUserChoice() {
        String userChoice = "Z10";
        TicketBooking ticketBooking = mock(TicketBooking.class);
        SeatingConfigurationInput seatingConfigurationInput = new SeatingConfigurationInput("Argo", 10, 10);

        boolean result = cinemaBookingManager.rebookSeats(ticketBooking, userChoice, 2, seatingConfigurationInput);

        assertFalse(result);
        verify(cinemaSeatingDisplay).println("Invalid Input. No seats available");
    }

    @Test
    void testRebookSeats_successfulRebooking() {
        String userChoice = "A01";
        TicketBooking ticketBooking = mock(TicketBooking.class);
        SeatingConfigurationInput seatingConfigurationInput = new SeatingConfigurationInput("Argo", 10, 10);
        when(seatBookingService.updateSeatBooking(2, ticketBooking, 0, 0)).thenReturn(Optional.of(mock(TicketBooking.class)));

        boolean result = cinemaBookingManager.rebookSeats(ticketBooking, userChoice, 2, seatingConfigurationInput);

        assertTrue(result);
        verify(seatBookingService).updateSeatBooking(2, ticketBooking, 0, 0);
    }

    @Test
    void testConfirmBooking() {
        String bookingId = "GIC0001";
        TicketBooking ticketBooking = mock(TicketBooking.class);
        when(ticketBooking.bookingId()).thenReturn(bookingId);

        cinemaBookingManager.confirmBooking(ticketBooking);
        verify(seatBookingService).processAndAddConfirmedBooking(ticketBooking);

        verify(cinemaSeatingDisplay).println("Booking Id: " + bookingId + " confirmed.");
    }

    @Test
    void testProcessUserChoice_EmptyUserChoice() {
        SeatingConfigurationInput seatingConfigurationInput = new SeatingConfigurationInput("Argo", 10, 10);
        TicketBooking ticketBooking = mock(TicketBooking.class);
        boolean result = cinemaBookingManager.processUserChoice("", ticketBooking, seatingConfigurationInput, 2);
        assertTrue(result);

    }

    @Test
    void testProcessUserChoice_FailedRebook() {
        SeatingConfigurationInput seatingConfigurationInput = new SeatingConfigurationInput("Argo", 10, 10);
        TicketBooking ticketBooking = mock(TicketBooking.class);
        boolean result = cinemaBookingManager.processUserChoice("1000", ticketBooking, seatingConfigurationInput, 2);
        assertFalse(result);

    }

    @Test
    void testProcessUserChoice_SuccessRebook() {
        SeatingConfigurationInput seatingConfigurationInput = new SeatingConfigurationInput("Argo", 10, 10);
        TicketBooking ticketBooking = mock(TicketBooking.class);

        boolean result = cinemaBookingManager.processUserChoice("A01", ticketBooking, seatingConfigurationInput, 2);

        assertFalse(result);

    }

    @Test
    void testProcessBookingAttempt_Failed() {
        SeatingConfigurationInput seatingConfigurationInput = new SeatingConfigurationInput("Argo", 10, 10);
        when(seatBookingService.bookSeats(2, "Argo")).thenReturn(Optional.empty());

        cinemaBookingManager.processBookingAttempt(2, seatingConfigurationInput);

        verify(cinemaSeatingDisplay).println("Sorry, booking failed. Seats may no longer be available.");

    }

    @Test
    void testGetStartColumn_InvalidInput() {
        cinemaBookingManager.getStartColumn("aaa");

        verify(cinemaSeatingDisplay).println("Invalid Input. No seats available");
    }


}
