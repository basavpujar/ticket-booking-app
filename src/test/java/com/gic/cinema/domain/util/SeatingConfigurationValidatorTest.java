package com.gic.cinema.domain.util;

import com.gic.cinema.domain.model.SeatingConfigurationInput;
import com.gic.cinema.presentation.view.CinemaSeatingDisplay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SeatingConfigurationValidatorTest {

    @Mock
    private CinemaSeatingDisplay cinemaSeatingDisplayMock;

    private SeatingConfigurationValidator seatingConfigurationValidator;

    @BeforeEach
    void setUp() {
        seatingConfigurationValidator = new SeatingConfigurationValidator(cinemaSeatingDisplayMock);
    }

    @Test
    void testParseUserInputWithinRange_ValidInput() {
        String validInput = "10";
        int result = SeatingConfigurationValidator.parseUserInputWithinRange(validInput, 1, 50);
        assertEquals(10, result);
    }

    @Test
    void testParseUserInputWithinRange_InvalidInput() {
        String invalidInput = "100";
        int result = SeatingConfigurationValidator.parseUserInputWithinRange(invalidInput, 1, 50);
        assertEquals(-1, result);
    }

    @Test
    void testParseUserInputWithinRange_NonIntegerInput() {
        String invalidInput = "abc";
        int result = SeatingConfigurationValidator.parseUserInputWithinRange(invalidInput, 1, 50);
        assertEquals(-1, result);
    }

    @Test
    void testGetSeatsPerRow_ValidInput() {
        String seatsPerRow = "20";
        int result = SeatingConfigurationValidator.getSeatsPerRow(seatsPerRow);
        assertEquals(20, result);
    }

    @Test
    void testGetSeatsPerRow_InvalidInput() {
        String seatsPerRow = "60";
        int result = SeatingConfigurationValidator.getSeatsPerRow(seatsPerRow);
        assertEquals(-1, result);
    }

    @Test
    void testGetRowNumber_ValidInput() {
        String rowNumber = "10";
        int result = SeatingConfigurationValidator.getRowNumber(rowNumber);
        assertEquals(10, result);
    }

    @Test
    void testGetRowNumber_InvalidInput() {
        String rowNumber = "30";
        int result = SeatingConfigurationValidator.getRowNumber(rowNumber);
        assertEquals(-1, result);
    }

    @Test
    void testIsMovieTileValid_ValidInput() {
        String validMovieTitle = "Inception";
        assertTrue(SeatingConfigurationValidator.isMovieTileValid(validMovieTitle));
    }

    @Test
    void testIsMovieTileValid_InvalidInput() {
        String invalidMovieTitle = " ";
        assertFalse(SeatingConfigurationValidator.isMovieTileValid(invalidMovieTitle));
    }

    @Test
    void testHasValidInputFormat_ValidInput() {
        String[] validInput = {"Inception", "10", "20"};
        assertTrue(SeatingConfigurationValidator.hasValidInputFormat(validInput));
    }

    @Test
    void testHasValidInputFormat_InvalidInput() {
        String[] invalidInput = {"Inception", "10"};
        assertFalse(SeatingConfigurationValidator.hasValidInputFormat(invalidInput));
    }

    @Test
    void testIsInvalidNumber_ValidNumber() {
        int validNumber = 10;
        assertFalse(SeatingConfigurationValidator.isInvalidNUmber(validNumber));
    }

    @Test
    void testIsInvalidNumber_InvalidNumber() {
        int invalidNumber = -1;
        assertTrue(SeatingConfigurationValidator.isInvalidNUmber(invalidNumber));
    }

    @Test
    void testValidateUserInput_InvalidInputFormat() {
        String invalidInput = "Inception 30";

        SeatingConfigurationInput result = seatingConfigurationValidator.validateUserInput(invalidInput);

        assertNull(result);
        verify(cinemaSeatingDisplayMock, times(1)).println("Invalid input format.Please try again  or enter 0 to exit.");
    }

    @Test
    void testValidateUserInput_InvalidMovieTitle() {
        String invalidInput = " 30 10";

        SeatingConfigurationInput result = seatingConfigurationValidator.validateUserInput(invalidInput);

        assertNull(result);
        verify(cinemaSeatingDisplayMock, times(1)).println("Movie Title cannot be null or empty. Please try again  or enter 0 to exit.");
    }

    @Test
    void testValidateUserInput_InvalidRowValue() {
        String invalidInput = "Inception 30 10";

        SeatingConfigurationInput result = seatingConfigurationValidator.validateUserInput(invalidInput);

        assertNull(result);
        verify(cinemaSeatingDisplayMock, times(1)).println("Invalid Row value. Value should be between 1-26. Please try again  or enter 0 to exit.");
    }

    @Test
    void testValidateUserInput_InvalidSeatsPerRowValue() {
        String invalidInput = "Inception 20 60";

        SeatingConfigurationInput result = seatingConfigurationValidator.validateUserInput(invalidInput);

        assertNull(result);
        verify(cinemaSeatingDisplayMock, times(1)).println("Invalid Seats per row value. Value should be between 1-50. Please try again  or enter 0 to exit.");
    }


    @Test
    void testHasUserInputValidationFailed_InvalidRow() {
        char[][] seatingPlan = new char[10][10];
        SeatingConfigurationInput seatingInput = new SeatingConfigurationInput("Inception", 10, 10);
        boolean result = seatingConfigurationValidator.hasUserInputValidationFailed(-1, 5, seatingPlan, seatingInput);
        assertTrue(result);
        verify(cinemaSeatingDisplayMock, times(1))
                .println("Invalid seat selection, row selection needs to be between A and J");
    }

    @Test
    void testGetSeatingConfigurationFromUser_ExitInput() {
        String exitInput = "0";

        Optional<SeatingConfigurationInput> result = seatingConfigurationValidator.getSeatingConfigurationFromUser(exitInput);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetSeatingConfigurationFromUser_InvalidInput() {
        String invalidInput = "Argo 11 11";

        Optional<SeatingConfigurationInput> result = seatingConfigurationValidator.getSeatingConfigurationFromUser(invalidInput);

        assertTrue(result.isPresent());
    }

    @Test
    void testHasUserInputValidationFailed_InvalidColumn() {
        char[][] seatingPlan = new char[10][10];
        SeatingConfigurationInput seatingInput = new SeatingConfigurationInput("Inception", 10, 10);
        boolean result = seatingConfigurationValidator.hasUserInputValidationFailed(5, 20, seatingPlan, seatingInput);
        assertTrue(result);
        verify(cinemaSeatingDisplayMock, times(1))
                .println("Invalid seat selection, seat number needs to be between 1 and 10");
    }

    @Test
    void testHasUserInputValidationFailed_SeatAlreadyBooked() {
        char[][] seatingPlan = new char[10][10];
        seatingPlan[5][5] = 'C';
        SeatingConfigurationInput seatingInput = new SeatingConfigurationInput("Inception", 10, 10);
        boolean result = seatingConfigurationValidator.hasUserInputValidationFailed(5, 5, seatingPlan, seatingInput);
        assertTrue(result);
        verify(cinemaSeatingDisplayMock, times(1))
                .println("Invalid seat selection, seat is already booked");
    }


}
