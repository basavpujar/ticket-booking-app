package com.gic.cinema.presentation.output;

import com.gic.cinema.presentation.output.interfaces.OutputHandler;

public class ConsoleOutputHandler implements OutputHandler {
    @Override
    public void println(String message) {
        System.out.println(message);
    }

    @Override
    public void print(String message) {
        System.out.print(message);
    }

    @Override
    public void printf(String format, Object... args) {
        System.out.printf(format, args);
    }

}
