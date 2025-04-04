package com.gic.cinema;

import com.gic.cinema.controller.CinemaBookingManager;
import com.gic.cinema.domain.interfaces.BookingService;
import com.gic.cinema.domain.interfaces.Display;
import com.gic.cinema.domain.interfaces.InputHandler;
import com.gic.cinema.domain.util.SeatingConfigurationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CinemaBookingAppTest {

    @Mock
    private Display displayMock;

    @Mock
    private BookingService bookingServiceMock;

    @Mock
    private InputHandler inputHandlerMock;

    @Mock
    private SeatingConfigurationValidator seatingConfigValidatorMock;

    private CinemaBookingApp cinemaBookingApp;
    private CinemaBookingManager cinemaBookingManagerMock;

    @BeforeEach
    void setUp() {
        cinemaBookingManagerMock = mock(CinemaBookingManager.class);
        cinemaBookingApp = new CinemaBookingApp(displayMock, bookingServiceMock, inputHandlerMock, seatingConfigValidatorMock);
    }

    @Test
    void testRun_shouldCallStartMethodOfCinemaBookingManager() {
        CinemaBookingApp cinemaBookingApp = new CinemaBookingApp(cinemaBookingManagerMock);

        cinemaBookingApp.run();
        verify(cinemaBookingManagerMock, times(1)).start();
    }

    @Test
    void testCreateDefaultApp() {
        CinemaBookingApp defaultApp = CinemaBookingApp.createDefaultApp();
        assertNotNull(defaultApp);
    }


    @Test
    void testRun_withMockedDisplay() {
        cinemaBookingApp.run();
        verify(displayMock, times(1)).println("Please define movie and seating map in [Title] [Row] [SeatsPerRow] format:");
        verify(displayMock, times(1)).println("Thank you for using GIC Cinemas system. Bye!");
    }


}
