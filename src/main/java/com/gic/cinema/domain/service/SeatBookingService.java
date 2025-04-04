package com.gic.cinema.domain.service;

import com.gic.cinema.domain.interfaces.BookingService;
import com.gic.cinema.domain.interfaces.SeatingPlan;
import com.gic.cinema.domain.model.TicketBooking;
import com.gic.cinema.domain.util.BookingIdGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SeatBookingService implements BookingService {
    private final Map<String, TicketBooking> bookingDetailMap;
    private final BookingIdGenerator bookingIdGenerator;
    private SeatingPlan seatingPlan;

    public SeatBookingService() {
        this.bookingIdGenerator = new BookingIdGenerator();
        this.bookingDetailMap = new HashMap<>();

    }

    public Map<String, TicketBooking> getBookingDetailMap() {
        return bookingDetailMap;
    }

    @Override
    public void initializeSeating(int rows, int columns) {
        this.seatingPlan = new SeatingPlanService(rows, columns);
        seatingPlan.initializeSeating(rows, columns);
    }

    @Override
    public char[][] getSeatingPlan() {
        return seatingPlan.getSeatingPlan();
    }

    @Override
    public void updateAvailableSeats() {
        seatingPlan.updateAvailableSeats();
    }

    @Override
    public int getAvailableSeats() {
        return seatingPlan.getAvailableSeats();
    }

    public boolean areSeatsAvailable(int numberOfTickets) {
        int availableSeats = 0;
        char[][] seats = seatingPlan.getSeatingPlan();
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                if (seats[i][j] == '.') {
                    availableSeats++;
                }
            }
        }
        return availableSeats >= numberOfTickets;
    }

    public Optional<TicketBooking> bookSeats(int numberOfTickets, String movieTitle) {

        if (!areSeatsAvailable(numberOfTickets)) {
            return Optional.empty();
        }
        int seatsBooked = 0;
        StringBuilder seatingDetails = new StringBuilder();
        char[][] seats = seatingPlan.getSeatingPlan();
        for (int row = 0; row < seats.length; row++) {
            if (seatsBooked >= numberOfTickets) {
                break;
            }
            seatsBooked = bookSeatsInRowFromMiddle(row, numberOfTickets, seatsBooked, seatingDetails);
        }

        if (seatsBooked == numberOfTickets) {
            String bookingId = bookingIdGenerator.generateBookingId();
            return Optional.of(new TicketBooking(bookingId, numberOfTickets, movieTitle, seatingDetails.toString().trim()));
        }
        return Optional.empty();
    }

    private int bookSeats(int row, int column, int seatsBooked, StringBuilder seatingDetails) {
        char[][] seats = seatingPlan.getSeatingPlan();
        seats[row][column] = '0';
        seatsBooked++;
        seatingDetails.append((char) ('A' + row)).append(column + 1).append(" ");
        return seatsBooked;
    }


    public Optional<TicketBooking> updateSeatBooking(int numberOfTickets, TicketBooking ticketBookingDetails, int startRow, int startColumn) {
        if (!areSeatsAvailable(numberOfTickets)) {
            return Optional.empty();
        }
        int seatsBooked = 0;
        StringBuilder seatingDetails = new StringBuilder();
        char[][] seats = seatingPlan.getSeatingPlan();
        for (int row = startRow; row < seats.length; row++) {
            if (seatsBooked >= numberOfTickets) {
                break;
            }

            if (row == startRow) {
                seatsBooked = bookSeatsFromCenterToRight(row, startColumn, numberOfTickets, seatsBooked, seatingDetails);
            } else {
                seatsBooked = bookSeatsInRowFromMiddle(row, numberOfTickets, seatsBooked, seatingDetails);
            }
        }

        if (seatsBooked == numberOfTickets) {
            return Optional.of(new TicketBooking(ticketBookingDetails.bookingId(), numberOfTickets, ticketBookingDetails.movieTitle(), seatingDetails.toString().trim()));
        }

        return Optional.empty();
    }

    private int bookSeatsInRowFromMiddle(int row, int numberOfTickets, int seatsBooked, StringBuilder seatingDetails) {
        char[][] seats = seatingPlan.getSeatingPlan();
        int middleRow = seats[row].length / 2;
        seatsBooked = bookSeatsFromCenterToRight(row, middleRow, numberOfTickets, seatsBooked, seatingDetails);

        if (seatsBooked < numberOfTickets) {
            seatsBooked = bookSeatsFromCenterToLeft(row, middleRow, numberOfTickets, seatsBooked, seatingDetails);
        }
        return seatsBooked;
    }

    private int bookSeatsFromCenterToRight(int row, int column, int numberOfTickets, int seatsBooked, StringBuilder seatingDetails) {
        char[][] seats = seatingPlan.getSeatingPlan();
        for (int i = column; i < seats[row].length; i++) {
            if (seats[row][i] == '.') {
                seatsBooked = bookSeats(row, i, seatsBooked, seatingDetails);
                if (seatsBooked == numberOfTickets)
                    return seatsBooked;
            }
        }
        return seatsBooked;
    }


    private int bookSeatsFromCenterToLeft(int row, int middleRow, int numberOfTickets, int seatsBooked, StringBuilder seatingDetails) {
        char[][] seats = seatingPlan.getSeatingPlan();
        for (int i = middleRow - 1; i >= 0; i--) {
            if (seats[row][i] == '.') {
                seatsBooked = bookSeats(row, i, seatsBooked, seatingDetails);
                if (seatsBooked == numberOfTickets)
                    return seatsBooked;
            }
        }
        return seatsBooked;
    }

    public void processAndAddConfirmedBooking(TicketBooking ticketBookingDetails) {
        seatingPlan.updateAvailableSeats();
        updateBookedSeats(ticketBookingDetails.seatingDetails());
        bookingDetailMap.put(ticketBookingDetails.bookingId(), ticketBookingDetails);
    }

    private void updateBookedSeats(String seatingDetails) {
        char[][] seats = seatingPlan.getSeatingPlan();
        String[] bookedSeats = seatingDetails.split(" ");
        for (String seat : bookedSeats) {
            char row = seat.charAt(0);
            int col = Integer.parseInt(seat.substring(1));
            int rowIndex = row - 'A';
            if (rowIndex >= 0 && rowIndex < seats.length
                    && col <= seats[rowIndex].length) {
                seats[rowIndex][col - 1] = '#';
            }
        }
    }

    public void clearPreviousBookingDetails(TicketBooking ticketBookingDetails) {

        String[] bookedSeats = ticketBookingDetails.seatingDetails().split(" ");
        char[][] seats = seatingPlan.getSeatingPlan();
        for (String seat : bookedSeats) {
            char row = seat.charAt(0);
            int col = Integer.parseInt(seat.substring(1));
            int rowIndex = row - 'A';
            if (rowIndex >= 0 && rowIndex < seats.length
                    && col <= seats[rowIndex].length) {
                seats[rowIndex][col - 1] = '.';
            }
        }
    }

    public Optional<TicketBooking> getBookingDetails(String bookingId) {
        return Optional.ofNullable(bookingDetailMap.get(bookingId));
    }


}
