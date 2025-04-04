package com.gic.cinema;

import com.gic.cinema.controller.CinemaBookingManager;
import com.gic.cinema.domain.interfaces.BookingService;
import com.gic.cinema.domain.interfaces.Display;
import com.gic.cinema.domain.interfaces.InputHandler;
import com.gic.cinema.domain.service.SeatBookingService;
import com.gic.cinema.domain.util.SeatingConfigurationValidator;
import com.gic.cinema.presentation.input.UserInputHandler;
import com.gic.cinema.presentation.view.CinemaSeatingDisplay;

public class CinemaBookingApp {

    private final CinemaBookingManager bookingManager;

    public CinemaBookingApp(Display display, BookingService bookingService, InputHandler inputHandler, SeatingConfigurationValidator seatingConfigurationValidator) {
        this.bookingManager = new CinemaBookingManager(display, bookingService, inputHandler, seatingConfigurationValidator);
    }

    public CinemaBookingApp(CinemaBookingManager bookingManager) {
        this.bookingManager = bookingManager;
    }

    public static CinemaBookingApp createDefaultApp() {
        Display display = new CinemaSeatingDisplay();
        InputHandler inputHandler = new UserInputHandler();
        SeatingConfigurationValidator seatingConfigurationValidator = new SeatingConfigurationValidator(display);
        BookingService bookingService = new SeatBookingService();
        return new CinemaBookingApp(display, bookingService, inputHandler, seatingConfigurationValidator);
    }

    public static void main(String[] args) {
        CinemaBookingApp app = createDefaultApp();
        app.run();
    }

    public void run() {
        bookingManager.start();
    }
}
