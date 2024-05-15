package dev.mayuna.consoleparallax.impl;

import dev.mayuna.consoleparallax.BaseCommand;
import dev.mayuna.consoleparallax.CommandInvocationContext;
import org.jetbrains.annotations.NotNull;

public class TestCommand implements BaseCommand {

    public static final String NAME = "test";

    public boolean executed = false;

    @Override
    public @NotNull String getName() {
        return NAME;
    }

    @Override
    public void execute(@NotNull CommandInvocationContext context) {
        executed = true;
    }
}
