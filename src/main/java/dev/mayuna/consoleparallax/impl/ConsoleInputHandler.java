package dev.mayuna.consoleparallax.impl;

import dev.mayuna.consoleparallax.InputHandler;
import org.jetbrains.annotations.NotNull;

import java.io.Console;
import java.util.Scanner;

/**
 * Console implementation for {@link InputHandler}<br>
 * Uses {@link Console#readLine()} to read input
 */
public final class ConsoleInputHandler implements InputHandler {

    private final Scanner scanner;

    public ConsoleInputHandler() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public @NotNull String getNextInput() {
        return scanner.nextLine();
    }
}
