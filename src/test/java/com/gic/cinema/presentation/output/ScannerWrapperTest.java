package com.gic.cinema.presentation.output;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScannerWrapperTest {


    private ScannerWrapper scannerWrapper;
    @Mock
    private Scanner scanner;

    @BeforeEach
    void setup() {
        scanner = mock(Scanner.class);
        scannerWrapper = new ScannerWrapper(scanner);
    }

    @Test
    void testNextLine() {
        when(scanner.nextLine()).thenReturn("SCREEN");
        assertEquals("SCREEN", scannerWrapper.nextLine());
        verify(scanner, times(1)).nextLine();
    }
}