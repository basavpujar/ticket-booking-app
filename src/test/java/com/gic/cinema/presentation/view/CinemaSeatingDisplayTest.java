package com.gic.cinema.presentation.view;

import com.gic.cinema.domain.model.TicketBooking;
import com.gic.cinema.presentation.output.interfaces.OutputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CinemaSeatingDisplayTest {

    @Mock
    private OutputHandler outputHandlerMock;

    private CinemaSeatingDisplay cinemaSeatingDisplay;

    @BeforeEach
    void setUp() {
        cinemaSeatingDisplay = new CinemaSeatingDisplay(outputHandlerMock);
    }

    @Test
    void testDisplayWelcomeScreen() {
        String movieTitle = "Avengers";
        int seatsAvailable = 50;

        cinemaSeatingDisplay.displayWelcomeScreen(movieTitle, seatsAvailable);

        verify(outputHandlerMock).println("\nWelcome to GIC Cinemas");
        verify(outputHandlerMock).println("[1] Book tickets for Avengers (50 seats available)");
        verify(outputHandlerMock).println("[2] Check Bookings");
        verify(outputHandlerMock).println("[3] Exit");
        verify(outputHandlerMock).println("Please enter your selection:");
    }

    @Test
    void testDisplaySeatPlan() {
        char[][] seatingPlan = new char[][]{
                {'A', 'A', 'A', 'A'},
                {'B', 'B', 'B', 'B'},
                {'C', 'C', 'C', 'C'}
        };

        cinemaSeatingDisplay.displaySeatPlan(seatingPlan);

        verify(outputHandlerMock).println("Selected seats:");
        verify(outputHandlerMock, times(2)).println(anyString()); // for dashed lines and row numbers
        verify(outputHandlerMock, times(27)).printf(anyString(), any()); // for printing seating rows
    }

    @Test
    void testPrintBookingDetails() {
        TicketBooking ticketBooking = mock(TicketBooking.class);
        when(ticketBooking.bookingId()).thenReturn("12345");

        cinemaSeatingDisplay.printBookingDetails(ticketBooking);

        verify(outputHandlerMock).println("Booking Id: 12345");
    }

    @Test
    void testDisplayBookingSummary() {
        char[][] seatingPlan = new char[][]{
                {'A', 'A', 'A', 'A'},
                {'B', 'B', 'B', 'B'},
                {'C', 'C', 'C', 'C'}
        };
        TicketBooking ticketBooking = mock(TicketBooking.class);
        when(ticketBooking.bookingId()).thenReturn("12345");
        when(ticketBooking.movieTitle()).thenReturn("Avengers");

        cinemaSeatingDisplay.displayBookingSummary(2, seatingPlan, ticketBooking);

        verify(outputHandlerMock).println("Successfully reserved 2 Avengers tickets.");
        verify(outputHandlerMock).println("Booking Id: 12345");
        verify(outputHandlerMock).println("Selected seats:");
    }

    @Test
    void testPrintDashedLine() {
        int totalSeats = 5;

        cinemaSeatingDisplay.printDashedLine(totalSeats);

        verify(outputHandlerMock, times(6)).printf("%-3c", '-');
        verify(outputHandlerMock).print("\n");
    }

    @Test
    void testPrintSeatingRowNumbers() {
        int totalSeats = 4;

        cinemaSeatingDisplay.printSeatingRowNumbers(totalSeats);

        verify(outputHandlerMock).printf("%-3c", ' ');
        for (int i = 1; i <= totalSeats; i++) {
            verify(outputHandlerMock).printf("%-3d", i);
        }
        verify(outputHandlerMock).print("\n");
    }

    @Test
    void testPrintSeatingRows() {
        char[][] seatingPlan = new char[][]{
                {'A', 'A', 'A', 'A'},
                {'B', 'B', 'B', 'B'},
                {'C', 'C', 'C', 'C'}
        };

        cinemaSeatingDisplay.printSeatingRows(seatingPlan);

        verify(outputHandlerMock, times(4)).printf("%-3c", 'A');
        verify(outputHandlerMock, times(4)).printf("%-3c", 'B');
        verify(outputHandlerMock, times(4)).printf("%-3c", 'C');
    }

    @Test
    void testDisplaySeatPlanWithBookedSeats() {
        char[][] seatingPlan = new char[][]{
                {'A', '.', '.', '.'},
                {'B', '.', '.', '.'},
                {'C', '.', '.', '.'}
        };
        String seatingDetails = "A1 B2";

        cinemaSeatingDisplay.displaySeatPlan(seatingPlan, seatingDetails);

        verify(outputHandlerMock).println("Selected seats:");
        verify(outputHandlerMock, times(2)).println(anyString());
        verify(outputHandlerMock, times(27)).printf(anyString(), any());
    }

    @Test
    void testCreateSeatingPlanCopy() {
        char[][] seatingPlan = new char[][]{
                {'A', 'A', 'A', 'A'},
                {'B', 'B', 'B', 'B'},
                {'C', 'C', 'C', 'C'}
        };

        char[][] seatingPlanCopy = cinemaSeatingDisplay.createSeatingPlanCopy(seatingPlan);

        assertNotSame(seatingPlan, seatingPlanCopy);
        assertArrayEquals(seatingPlan, seatingPlanCopy);
    }
}
