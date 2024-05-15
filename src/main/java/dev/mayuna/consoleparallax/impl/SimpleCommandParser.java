package dev.mayuna.consoleparallax.impl;

import dev.mayuna.consoleparallax.CommandParseResult;
import dev.mayuna.consoleparallax.CommandParser;
import org.jetbrains.annotations.NotNull;

/**
 * Simple command parser which takes the command, trims it and splits it by spaces<br>
 * First element is the command name, the rest are arguments
 */
public final class SimpleCommandParser implements CommandParser {

    @Override
    public @NotNull CommandParseResult parseCommand(@NotNull String command) {
        command = command.trim().replaceAll(" +", " ");
        String[] splits = command.split(" "); // TODO: Test if returns empty strings when no name/arguments
        String commandName = splits[0];
        String[] arguments = new String[splits.length - 1];

        if (arguments.length > 0) {
            System.arraycopy(splits, 1, arguments, 0, arguments.length);
        }

        return new CommandParseResult(commandName, arguments);
    }
}
