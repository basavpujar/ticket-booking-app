package com.gic.cinema.presentation.output;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsoleOutputHandlerTest {

    private final ByteArrayOutputStream outStreamContent = new ByteArrayOutputStream();
    private final PrintStream originalOutputStream = System.out;

    private ConsoleOutputHandler consoleOutputHandler;

    @BeforeEach
    void setup() {
        System.setOut(new PrintStream(outStreamContent));
        consoleOutputHandler = new ConsoleOutputHandler();
    }

    @Test
    void testPrintln() {
        consoleOutputHandler.println("SCREEN");
        assertEquals("SCREEN" + System.lineSeparator(), outStreamContent.toString());
    }

    @Test
    void testPrint() {
        consoleOutputHandler.print("Book ");
        consoleOutputHandler.print("Tickets");
        assertEquals("Book Tickets", outStreamContent.toString());
    }

    @Test
    void testPrintf() {
        consoleOutputHandler.printf("%02d", 3);
        assertEquals("03", outStreamContent.toString());
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOutputStream);
    }
}