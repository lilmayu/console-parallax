package dev.mayuna.consoleparallax.impl;

import dev.mayuna.consoleparallax.CommandParseResult;
import dev.mayuna.consoleparallax.CommandParser;
import org.jetbrains.annotations.NotNull;

public class TestCommandParser implements CommandParser {

    @Override
    public @NotNull CommandParseResult parseCommand(@NotNull String command) {
        return new SimpleCommandParser().parseCommand(command);
    }
}
