package com.gic.cinema.presentation.input;

import com.gic.cinema.domain.interfaces.InputHandler;
import com.gic.cinema.presentation.output.ConsoleOutputHandler;
import com.gic.cinema.presentation.output.ScannerWrapper;
import com.gic.cinema.presentation.output.interfaces.OutputHandler;

import static com.gic.cinema.constants.AppConstants.INVALID_VALUE;

public class UserInputHandler implements InputHandler {

    private final ScannerWrapper scanner;
    private final OutputHandler outputHandler;

    public UserInputHandler() {
        outputHandler = new ConsoleOutputHandler();
        scanner = new ScannerWrapper();
    }

    public UserInputHandler(ScannerWrapper scanner, OutputHandler outputHandler) {
        this.scanner = scanner;
        this.outputHandler = outputHandler;
    }

    public int getUserSelection() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return INVALID_VALUE;
        }
    }

    @Override
    public String getUserAcceptChoice() {
        return scanner.nextLine().trim();
    }

    public int getNumberOfTickets() {
        promptForNumberOfSeats();
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return INVALID_VALUE;
            }

            try {
                int numberOfTickets = Integer.parseInt(input);
                if (numberOfTickets > 0)
                    return numberOfTickets;
                else
                    promptForNumberOfSeats();
            } catch (NumberFormatException e) {
                promptForNumberOfSeats();
            }
        }
    }

    public void promptForNumberOfSeats() {
        outputHandler.println("Enter number of tickets to book, or enter blank to go back to main menu");
    }

}
