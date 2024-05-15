package dev.mayuna.consoleparallax;

import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

/**
 * Holds information about the command invocation
 */
@Getter
public final class CommandInvocationContext {

    private final ConsoleParallax consoleParallax;
    private final CommandParseResult commandParseResult;

    /**
     * Creates a new CommandInvocationContext
     *
     * @param consoleParallax    ConsoleParallax instance
     * @param commandParseResult Command parse result
     */
    public CommandInvocationContext(@NotNull @NonNull ConsoleParallax consoleParallax, @NotNull @NonNull CommandParseResult commandParseResult) {
        this.consoleParallax = consoleParallax;
        this.commandParseResult = commandParseResult;
    }

    /**
     * Gets the name of the command from {@link CommandParseResult}
     *
     * @return Command name
     */
    public String getCommandName() {
        return commandParseResult.getCommandName();
    }

    /**
     * Gets the arguments of the command from {@link CommandParseResult}
     *
     * @return Command arguments
     */
    public String[] getArguments() {
        return commandParseResult.getArguments();
    }
}
