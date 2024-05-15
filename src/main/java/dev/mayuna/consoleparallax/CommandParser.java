package dev.mayuna.consoleparallax;

import org.jetbrains.annotations.NotNull;

/**
 * Interface for parsing commands
 */
public interface CommandParser {

    /**
     * Parses a command
     *
     * @param command Non-null command to parse
     *
     * @return Non-null parsed command
     */
    @NotNull
    CommandParseResult parseCommand(@NotNull String command);

}
