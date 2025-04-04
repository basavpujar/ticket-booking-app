package com.gic.cinema.domain.util;

import com.gic.cinema.domain.interfaces.Display;
import com.gic.cinema.domain.model.SeatingConfigurationInput;

import java.util.Optional;

import static com.gic.cinema.constants.AppConstants.*;

public class SeatingConfigurationValidator {
    private final Display cinemaSeatingDisplay;

    public SeatingConfigurationValidator(Display cinemaSeatingDisplay) {
        this.cinemaSeatingDisplay = cinemaSeatingDisplay;
    }

    static int parseUserInputWithinRange(String input, int min, int max) {
        try {
            int value = Integer.parseInt(input);
            if (value < min || value > max) {
                return INVALID_VALUE;
            }
            return value;
        } catch (NumberFormatException e) {
            return INVALID_VALUE;
        }
    }

    public static int getSeatsPerRow(String seatsPerRow) {
        return parseUserInputWithinRange(seatsPerRow, 1, MAX_SEATS_PER_ROW);
    }

    public static int getRowNumber(String rowNumber) {
        return parseUserInputWithinRange(rowNumber, 1, MAX_ROWS);
    }

    public static boolean isMovieTileValid(String movieTitle) {
        return movieTitle != null && !movieTitle.trim().isEmpty();
    }

    public static boolean hasValidInputFormat(String[] inputData) {
        return inputData.length == INPUT_DATA_LENGTH;
    }

    public static boolean isInvalidNUmber(int number) {
        return number == INVALID_VALUE;
    }

    public Optional<SeatingConfigurationInput> getSeatingConfigurationFromUser(String userInput) {
        SeatingConfigurationInput validatedInput = validateUserInput(userInput);
        return Optional.ofNullable(validatedInput);
    }

    SeatingConfigurationInput validateUserInput(String userInput) {
        String[] tokens = userInput.split(" ");
        if (!hasValidInputFormat(tokens)) {
            cinemaSeatingDisplay.println(INCORRECT_INPUT_FORMAT);
            return null;
        }

        String movieTitle = tokens[0];
        if (!isMovieTileValid(movieTitle)) {
            cinemaSeatingDisplay.println(EMPTY_MOVIE_TITLE_ERROR);
            return null;
        }

        int rows = getRowNumber(tokens[1]);
        if (isInvalidNUmber(rows)) {
            cinemaSeatingDisplay.println(INVALID_ROW_NUMBER_INPUT);
            return null;
        }

        int seatsPerRow = getSeatsPerRow(tokens[2]);
        if (isInvalidNUmber(seatsPerRow)) {
            cinemaSeatingDisplay.println(INVALID_SEATS_PER_ROW_INPUT);
            return null;
        }

        return new SeatingConfigurationInput(movieTitle, rows, seatsPerRow);
    }

    public boolean hasUserInputValidationFailed(int startRow, int startColumn, char[][] seatingPlan, SeatingConfigurationInput seatingInput) {
        boolean validationFailed = false;
        if (startRow < 0 || startRow >= seatingPlan.length) {
            cinemaSeatingDisplay.println(INVALID_SEAT_ROW_SELECTION + (char) ('A' + seatingInput.rows() - 1));
            validationFailed = true;
        } else if (startColumn < 0 || startColumn >= seatingPlan[startRow].length) {
            cinemaSeatingDisplay.println(INVALID_SEAT_NUMBER_SELECTION + seatingInput.seatsPerRow());
            validationFailed = true;
        } else if (seatingPlan[startRow][startColumn] != '.') {
            cinemaSeatingDisplay.println(SEAT_ALREADY_BOOKED_ERROR_MESSAGE);
            validationFailed = true;
        }
        return validationFailed;
    }

}
