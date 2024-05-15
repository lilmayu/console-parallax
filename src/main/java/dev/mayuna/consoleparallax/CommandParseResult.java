package dev.mayuna.consoleparallax;

import org.jetbrains.annotations.NotNull;

/**
 * Holds information about parsed command input
 */
public final class CommandParseResult {

    private final String commandName;
    private final String[] arguments;

    /**
     * Creates a new CommandParseResult
     *
     * @param commandName Non-null command name
     * @param arguments   Non-null command arguments
     */
    public CommandParseResult(@NotNull String commandName, @NotNull String[] arguments) {
        this.commandName = commandName;
        this.arguments = arguments;
    }

    /**
     * Returns parsed command name
     *
     * @return Non-null command name
     */
    public @NotNull String getCommandName() {
        return commandName;
    }

    /**
     * Returns parsed command arguments
     *
     * @return Non-null command arguments
     */
    public @NotNull String[] getArguments() {
        return arguments;
    }
}
