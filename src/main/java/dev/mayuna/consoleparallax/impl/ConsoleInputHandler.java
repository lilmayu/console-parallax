package dev.mayuna.consoleparallax.impl;

import dev.mayuna.consoleparallax.InputHandler;
import org.jetbrains.annotations.NotNull;

import java.io.Console;

/**
 * Console implementation for {@link InputHandler}<br>
 * Uses {@link Console#readLine()} to read input
 */
public final class ConsoleInputHandler implements InputHandler {

    @Override
    public @NotNull String getNextInput() {
        Console console = System.console();

        if (console == null) {
            throw new IllegalStateException("Console is not available");
        }

        return console.readLine();
    }
}
