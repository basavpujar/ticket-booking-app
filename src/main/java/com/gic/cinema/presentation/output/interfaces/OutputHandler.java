package com.gic.cinema.presentation.output.interfaces;

public interface OutputHandler {
    void println(String message);

    void print(String message);

    void printf(String format, Object... args);
}
