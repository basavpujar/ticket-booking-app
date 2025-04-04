package com.gic.cinema.presentation.output;

import java.util.Scanner;

public class ScannerWrapper {
    private final Scanner scanner;

    public ScannerWrapper() {
        this.scanner = new Scanner(System.in);
    }

    public ScannerWrapper(Scanner scanner) {
        this.scanner = scanner;
    }

    public String nextLine() {
        return scanner.nextLine();
    }
}

