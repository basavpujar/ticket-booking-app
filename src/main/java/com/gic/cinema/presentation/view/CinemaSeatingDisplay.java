package com.gic.cinema.presentation.view;

import com.gic.cinema.domain.interfaces.Display;
import com.gic.cinema.domain.model.TicketBooking;
import com.gic.cinema.presentation.output.ConsoleOutputHandler;
import com.gic.cinema.presentation.output.interfaces.OutputHandler;

import static com.gic.cinema.constants.AppConstants.*;

public class CinemaSeatingDisplay implements Display {

    private final OutputHandler outputHandler;

    public CinemaSeatingDisplay(OutputHandler outputHandler) {
        this.outputHandler = outputHandler;
    }

    public CinemaSeatingDisplay() {
        outputHandler = new ConsoleOutputHandler();
    }

    protected void printScreenHeader(int totalSeats) {
        printDashedLine(totalSeats);
        String screenText = "SCREEN";
        String screenTextWithPadding = String.join("  ", screenText.split(""));
        int screenStartPosition = (totalSeats - screenText.length()) / 2;

        for (int i = 0; i < screenStartPosition + 1; i++) {
            outputHandler.print("   ");
        }
        outputHandler.println(screenTextWithPadding);
        printDashedLine(totalSeats);

    }

    public void printDashedLine(int totalSeats) {
        for (int i = 0; i < totalSeats + 1; i++) {
            outputHandler.printf(CHAR_FORMAT, '-');
        }
        outputHandler.print(System.lineSeparator());
    }

    public void displayWelcomeScreen(String movieTitle, int seatsAvailable) {
        outputHandler.println(System.lineSeparator() + WELCOME_MESSAGE);
        outputHandler.println(USER_CHOICE_1 + movieTitle + " (" + seatsAvailable + SEATS_AVAILABLE_MESSAGE + ")");
        outputHandler.println(USER_CHOICE_2);
        outputHandler.println(USER_CHOICE_3);
        outputHandler.println(USER_CONFIRMATION);
    }

    public void displaySeatPlan(char[][] seatingPlan) {
        outputHandler.println(SELECTED_SEATS);
        printScreenHeader(seatingPlan[0].length);
        printSeatingRows(seatingPlan);
        printSeatingRowNumbers(seatingPlan[0].length);
    }

    protected void printSeatingRowNumbers(int length) {
        outputHandler.printf(CHAR_FORMAT, ' ');
        for (int i = 1; i <= length; i++) {
            outputHandler.printf(NUMBER_FORMAT, i);
        }
        outputHandler.print(System.lineSeparator());
    }

    protected void printSeatingRows(char[][] seatingPlan) {
        for (int i = seatingPlan.length - 1; i >= 0; i--) {
            outputHandler.print((char) ('A' + i) + "  ");
            for (int j = 0; j < seatingPlan[i].length; j++) {
                outputHandler.printf(CHAR_FORMAT, seatingPlan[i][j]);
            }
            outputHandler.print(System.lineSeparator());
        }
    }

    public void printBookingDetails(TicketBooking ticketBooking) {
        outputHandler.println(BOOKING_ID + ticketBooking.bookingId());
    }

    public void displaySeatPlan(char[][] seatingPlan, String seatingDetails) {
        outputHandler.println(SELECTED_SEATS);
        printScreenHeader(seatingPlan[0].length);
        printSeatingRows(seatingPlan, seatingDetails);
        printSeatingRowNumbers(seatingPlan[0].length);
    }

    private void printSeatingRows(char[][] originalSeatingPlan, String seatingDetails) {
        char[][] seatingPlan = createSeatingPlanCopy(originalSeatingPlan);
        String[] bookedSeats = seatingDetails.split(" ");

        for (String seat : bookedSeats) {
            char row = seat.charAt(0);
            int col = Integer.parseInt(seat.substring(1));
            int rowIndex = row - 'A';
            if (rowIndex >= 0 && rowIndex < seatingPlan.length && col <= seatingPlan[rowIndex].length) {
                seatingPlan[rowIndex][col - 1] = 'C';
            }
        }

        for (int i = seatingPlan.length - 1; i >= 0; i--) {
            outputHandler.print((char) ('A' + i) + "  ");
            for (int j = 0; j < seatingPlan[i].length; j++) {
                outputHandler.printf(CHAR_FORMAT, seatingPlan[i][j]);
            }
            outputHandler.print(System.lineSeparator());
        }


    }

    char[][] createSeatingPlanCopy(char[][] originalSeatingPlan) {
        char[][] seatingPlanCopy = new char[originalSeatingPlan.length][originalSeatingPlan[0].length];

        for (int i = 0; i < originalSeatingPlan.length; i++) {
            for (int j = 0; j < originalSeatingPlan[i].length; j++) {
                seatingPlanCopy[i][j] = originalSeatingPlan[i][j];
            }
        }
        return seatingPlanCopy;
    }

    public void displayBookingSummary(int numberOfTickets, char[][] seatingPlan, TicketBooking ticketBookingDetails) {
        outputHandler.println(SUCCESSFUL_RESERVATION_MESSAGE + numberOfTickets + " " + ticketBookingDetails.movieTitle() + " tickets.");
        outputHandler.println(BOOKING_ID + ticketBookingDetails.bookingId());
        displaySeatPlan(seatingPlan);
    }

    @Override
    public void println(String message) {
        outputHandler.println(message);
    }
}
