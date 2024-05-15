package dev.mayuna.consoleparallax;

import org.jetbrains.annotations.NotNull;

/**
 * Interface for handling output.
 */
public interface OutputHandler {

    /**
     * Prints an info message.
     *
     * @param message Message to print
     */
    void info(@NotNull String message);

    /**
     * Prints a error message.
     *
     * @param message Message to print
     */
    void error(@NotNull String message);
}
