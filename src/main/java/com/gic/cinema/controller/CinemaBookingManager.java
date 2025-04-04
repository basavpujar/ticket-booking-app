package com.gic.cinema.controller;

import com.gic.cinema.domain.interfaces.BookingService;
import com.gic.cinema.domain.interfaces.Display;
import com.gic.cinema.domain.interfaces.InputHandler;
import com.gic.cinema.domain.model.SeatingConfigurationInput;
import com.gic.cinema.domain.model.TicketBooking;
import com.gic.cinema.domain.util.SeatingConfigurationValidator;

import java.util.Optional;

import static com.gic.cinema.constants.AppConstants.*;

public class CinemaBookingManager {

    private final Display cinemaSeatingDisplay;
    private final BookingService seatBookingService;
    private final InputHandler userInputHandler;
    private final SeatingConfigurationValidator seatingConfigurationValidator;

    public CinemaBookingManager(Display display, BookingService bookingService, InputHandler inputHandler, SeatingConfigurationValidator seatingConfigurationValidator) {
        this.cinemaSeatingDisplay = display;
        this.seatBookingService = bookingService;
        this.userInputHandler = inputHandler;
        this.seatingConfigurationValidator = seatingConfigurationValidator;
    }

    public void start() {
        cinemaSeatingDisplay.println(STARTUP_MESSAGE);
        while (true) {
            String userInput = userInputHandler.getUserAcceptChoice();
            if (userInput == null || userInput.equals("0")) {
                cinemaSeatingDisplay.println(APP_EXIT_MESSAGE);
                return;
            }
            Optional<SeatingConfigurationInput> seatingInput = seatingConfigurationValidator.getSeatingConfigurationFromUser(userInput);
            if (seatingInput.isEmpty()) {
                continue;
            }
            SeatingConfigurationInput seatingConfigurationInput = seatingInput.get();
            seatBookingService.initializeSeating(seatingConfigurationInput.rows(), seatingConfigurationInput.seatsPerRow());
            processWelcomeScreen(seatingConfigurationInput);
            break;
        }
    }

    public void processWelcomeScreen(SeatingConfigurationInput seatingConfigurationInput) {
        boolean exit = false;
        while (!exit) {
            String movieTitle = seatingConfigurationInput.movieTitle();
            int availableSeats = seatBookingService.getAvailableSeats();
            cinemaSeatingDisplay.displayWelcomeScreen(movieTitle, availableSeats);
            int userSelection = userInputHandler.getUserSelection();
            exit = processUserSelection(userSelection, seatingConfigurationInput);
        }

    }

    boolean processUserSelection(int userSelection, SeatingConfigurationInput seatingConfigurationInput) {
        switch (userSelection) {
            case 1 -> bookTickets(seatingConfigurationInput);
            case 2 -> checkBooking();
            case 3 -> {
                cinemaSeatingDisplay.println(APP_EXIT_MESSAGE);
                return true;
            }
            default -> cinemaSeatingDisplay.println(INVALID_CHOICE_MESSAGE);
        }

        return false;
    }

    void checkBooking() {
        cinemaSeatingDisplay.println(CHECK_BOOKING_MESSAGE);
        String bookingId = userInputHandler.getUserAcceptChoice();
        if (bookingId != null && !bookingId.isEmpty()) {
            seatBookingService.getBookingDetails(bookingId).ifPresentOrElse(
                    booking -> {
                        cinemaSeatingDisplay.printBookingDetails(booking);
                        cinemaSeatingDisplay.displaySeatPlan(seatBookingService.getSeatingPlan(), booking.seatingDetails());
                    },
                    () -> cinemaSeatingDisplay.println(BOOKING_NOT_FOUND_ERROR)
            );
        }
    }

    void bookTickets(SeatingConfigurationInput seatingInput) {
        while (true) {
            int numberOfTickets = userInputHandler.getNumberOfTickets();
            if (isInvalidNumber(numberOfTickets)) {
                //cinemaSeatingDisplay.println(INVALID_NUMBER_OF_TICKETS);
                return;
            }

            if (!hasSufficientSeats(numberOfTickets)) {
                continue;
            }

            if (processBookingAttempt(numberOfTickets, seatingInput))
                break;
        }
    }

    boolean isInvalidNumber(int numberOfTickets) {
        return numberOfTickets == INVALID_VALUE;
    }

    private boolean hasSufficientSeats(int numberOfTickets) {
        int availableSeats = seatBookingService.getAvailableSeats();
        if (numberOfTickets > availableSeats) {
            cinemaSeatingDisplay.println(SORRY_MESSAGE + availableSeats + SEATS_AVAILABLE);
            return false;
        }
        return true;
    }

    boolean processBookingAttempt(int numberOfTickets, SeatingConfigurationInput seatingInput) {
        Optional<TicketBooking> bookingDetails = seatBookingService.bookSeats(numberOfTickets, seatingInput.movieTitle());

        if (bookingDetails.isPresent()) {
            handleSuccessfulBooking(numberOfTickets, seatingInput, bookingDetails.get());
            return true;
        }

        cinemaSeatingDisplay.println(BOOKING_FAILED);
        return false;
    }

    void handleSuccessfulBooking(int numberOfTickets, SeatingConfigurationInput seatingInput, TicketBooking ticketBookingDetails) {
        displayBookingSummary(numberOfTickets, seatingInput, ticketBookingDetails);
        cinemaSeatingDisplay.println(SEAT_SELECTION_RETRY_MESSAGE);

        while (true) {
            String userChoice = userInputHandler.getUserAcceptChoice();
            boolean exit = processUserChoice(userChoice, ticketBookingDetails, seatingInput, numberOfTickets);
            if (exit) break;
        }
    }

    private void displayBookingSummary(int numberOfTickets, SeatingConfigurationInput seatingInput, TicketBooking ticketBookingDetails) {
        cinemaSeatingDisplay.displayBookingSummary(numberOfTickets, seatBookingService.getSeatingPlan(), ticketBookingDetails);
    }

    boolean processUserChoice(String userChoice, TicketBooking ticketBookingDetails, SeatingConfigurationInput seatingInput, int numberOfTickets) {
        if (userChoice == null) {
            return true;
        }

        if (userChoice.isEmpty()) {
            confirmBooking(ticketBookingDetails);
            return true;
        }
        if (isInvalidUserChoice(userChoice)) {
            cinemaSeatingDisplay.println(INCORRECT_SEATNUMBER_FORMAT);
            return false;
        }

        boolean bookingSuccessful = rebookSeats(ticketBookingDetails, userChoice, numberOfTickets, seatingInput);
        if (!bookingSuccessful) {
            cinemaSeatingDisplay.println(SEAT_SELECTION_RETRY_MESSAGE);
        }
        return bookingSuccessful;
    }

    void confirmBooking(TicketBooking ticketBookingDetails) {
        seatBookingService.processAndAddConfirmedBooking(ticketBookingDetails);
        cinemaSeatingDisplay.println(BOOKING_ID + ticketBookingDetails.bookingId() + CONFIRMATION_MESSAGE);
    }

    boolean rebookSeats(TicketBooking ticketBookingDetails, String userChoice, int numberOfTickets, SeatingConfigurationInput seatingInput) {
        seatBookingService.clearPreviousBookingDetails(ticketBookingDetails);
        int startRow = getStartRow(userChoice);
        int startColumn = getStartColumn(userChoice);

        char[][] seatingPlan = seatBookingService.getSeatingPlan();

        if (isInvalidSeatSelection(startRow, startColumn, seatingPlan, seatingInput)) {
            return false;
        }

        return processReBooking(ticketBookingDetails, numberOfTickets, startRow, startColumn, seatingInput);
    }

    private boolean isInvalidUserChoice(String userChoice) {
        return userChoice.length() != MIN_SEAT_NUMBER_LEN;
    }

    private int getStartRow(String userChoice) {
        return userChoice.charAt(0) - 'A';
    }

    int getStartColumn(String userChoice) {
        try {
            return Integer.parseInt(userChoice.substring(1)) - 1;
        } catch (NumberFormatException e) {
            cinemaSeatingDisplay.println(NO_SEATS_MESSAGE);
            return INVALID_VALUE;
        }
    }

    private boolean isInvalidSeatSelection(int startRow, int startColumn, char[][] seatingPlan, SeatingConfigurationInput seatingInput) {
        return seatingConfigurationValidator.hasUserInputValidationFailed(startRow, startColumn, seatingPlan, seatingInput);
    }

    private boolean processReBooking(TicketBooking ticketBookingDetails, int numberOfTickets, int startRow, int startColumn, SeatingConfigurationInput seatingInput) {
        Optional<TicketBooking> updatedTicketBooking = seatBookingService.updateSeatBooking(numberOfTickets, ticketBookingDetails, startRow, startColumn);

        updatedTicketBooking.ifPresentOrElse(
                updatedBooking -> handleSuccessfulBooking(numberOfTickets, seatingInput, updatedBooking),
                () -> cinemaSeatingDisplay.println(NO_SEATS_MESSAGE)
        );

        return updatedTicketBooking.isPresent();
    }

}
