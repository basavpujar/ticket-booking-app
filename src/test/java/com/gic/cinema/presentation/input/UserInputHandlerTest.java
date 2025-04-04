package com.gic.cinema.presentation.input;

import com.gic.cinema.presentation.output.ScannerWrapper;
import com.gic.cinema.presentation.output.interfaces.OutputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.gic.cinema.constants.AppConstants.INVALID_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserInputHandlerTest {

    private UserInputHandler userInputHandler;
    @Mock
    private ScannerWrapper scanner;
    @Mock
    private OutputHandler outputHandler;

    @BeforeEach
    public void setup() {
        userInputHandler = new UserInputHandler(scanner, outputHandler);
    }

    @Test
    public void testGetUserSelection_ValidInput() {
        when(scanner.nextLine()).thenReturn("5");

        int result = userInputHandler.getUserSelection();

        assertEquals(5, result);
    }

    @Test
    public void testGetUserSelection_InvalidInput() {
        when(scanner.nextLine()).thenReturn("invalid");

        int result = userInputHandler.getUserSelection();

        assertEquals(INVALID_VALUE, result);
    }

    @Test
    public void testGetUserAcceptChoice() {
        when(scanner.nextLine()).thenReturn("yes");

        String result = userInputHandler.getUserAcceptChoice();

        assertEquals("yes", result);
    }

    @Test
    public void testGetNumberOfTickets_ValidInput() {
        when(scanner.nextLine()).thenReturn("3");

        int result = userInputHandler.getNumberOfTickets();

        assertEquals(3, result);
    }

    @Test
    public void testGetNumberOfTickets_InvalidInput() {
        when(scanner.nextLine()).thenReturn("invalid", "3");

        int result = userInputHandler.getNumberOfTickets();

        assertEquals(3, result);
    }

    @Test
    public void testGetNumberOfTickets_EmptyInput() {
        when(scanner.nextLine()).thenReturn(" ", "3");

        int result = userInputHandler.getNumberOfTickets();

        assertEquals(-1, result);
    }

    @Test
    public void testPromptForNumberOfSeats() {
        userInputHandler.promptForNumberOfSeats();

        verify(outputHandler).println("Enter number of tickets to book, or enter blank to go back to main menu");
    }

}