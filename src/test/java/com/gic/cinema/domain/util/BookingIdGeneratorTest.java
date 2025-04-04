package com.gic.cinema.domain.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.gic.cinema.constants.AppConstants.BOOKING_ID_PREFIX;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingIdGeneratorTest {
    private BookingIdGenerator bookingIdGenerator;

    @BeforeEach
    void setup() {
        bookingIdGenerator = new BookingIdGenerator();
    }

    @Test
    void testGenerateBookingId_format() {
        String bookingId = bookingIdGenerator.generateBookingId();
        assertEquals(BOOKING_ID_PREFIX + "0001", bookingId);
    }

    @Test
    void testGenerateBookingId_increment() {
        String bookingId1 = bookingIdGenerator.generateBookingId();
        String bookingId2 = bookingIdGenerator.generateBookingId();

        assertEquals(BOOKING_ID_PREFIX + "0002", bookingId2);
    }

}
