package dev.mayuna.consoleparallax;

import org.jetbrains.annotations.NotNull;

/**
 * Interface for handling arbitrary string input.
 */
public interface InputHandler {

    /**
     * Returns next input to be processed.<br>
     * Should block until input is available.
     *
     * @return String of next input
     */
    String getNextInput();
}
