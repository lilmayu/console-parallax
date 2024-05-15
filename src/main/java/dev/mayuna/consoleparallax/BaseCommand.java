package dev.mayuna.consoleparallax;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a command
 */
public interface BaseCommand {

    /**
     * Gets the name of the command by which it can be invoked
     *
     * @return Non-null command name
     */
    @NotNull
    String getName();

    /**
     * Gets the usage of the command
     *
     * @return Non-null usage
     */
    @NotNull
    default String getUsage() {
        return "No usage provided";
    }

    /**
     * Gets the syntax of the command
     *
     * @return Non-null syntax
     */
    @NotNull
    default String getSyntax() {
        return "No syntax provided";
    }

    /**
     * Gets the description of the command
     *
     * @return Non-null description
     */
    @NotNull
    default String getDescription() {
        return "No description provided";
    }

    /**
     * Executes the command
     *
     * @param context Non-null command invocation context
     */
    void execute(@NotNull CommandInvocationContext context);
}
