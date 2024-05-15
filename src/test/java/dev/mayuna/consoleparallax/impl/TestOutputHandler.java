package dev.mayuna.consoleparallax.impl;

import dev.mayuna.consoleparallax.OutputHandler;
import org.jetbrains.annotations.NotNull;

public class TestOutputHandler implements OutputHandler {

    @Override
    public void info(@NotNull String message) {
        System.out.println(message);
    }

    @Override
    public void error(@NotNull String message) {
        System.err.println(message);
    }
}
