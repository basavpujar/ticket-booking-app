package com.gic.cinema.domain.util;

import static com.gic.cinema.constants.AppConstants.BOOKING_ID_PREFIX;

public class BookingIdGenerator {
    private int currentBookingIdCounter;

    public BookingIdGenerator() {
        this.currentBookingIdCounter = 1;
    }

    public String generateBookingId() {
        return BOOKING_ID_PREFIX + String.format("%04d", currentBookingIdCounter++);
    }
}
