package dev.mayuna.consoleparallax.commands;

import dev.mayuna.consoleparallax.BaseCommand;
import dev.mayuna.consoleparallax.CommandInvocationContext;
import dev.mayuna.consoleparallax.ConsoleParallax;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Help command default implementation
 */
public class HelpCommand implements BaseCommand {

    @Override
    public @NotNull String getName() {
        return "help";
    }

    @Override
    public @NotNull String getUsage() {
        return "Displays all commands and their usages. If a command is specified, displays its description.";
    }

    @Override
    public @NotNull String getSyntax() {
        return "help [command]";
    }

    @Override
    public @NotNull String getDescription() {
        return "ConsoleParallax implemented command. Shows list of all commands and their descriptions. If a command is specified, display its description.";
    }

    @Override
    public void execute(@NotNull CommandInvocationContext context) {
        if (context.getArguments().length == 0) {
            showAllCommands(context);
            return;
        }

        showCommandDescription(context, context.getArguments()[0]);
    }

    /**
     * Shows all commands and their usages
     *
     * @param context Command invocation context
     */
    protected void showAllCommands(CommandInvocationContext context) {
        ConsoleParallax consoleParallax = context.getConsoleParallax();
        List<BaseCommand> commands = consoleParallax.getRegisteredCommands();

        consoleParallax.getOutputHandler().info("Number of commands: " + commands.size());

        synchronized (consoleParallax.getMutex()) {
            for (BaseCommand command : commands) {
                consoleParallax.getOutputHandler().info("  " + command.getName() + "\t" + command.getUsage());
            }
        }

        consoleParallax.getOutputHandler().info("Specify a command in the help command to see its description.");
    }

    /**
     * Shows the description of a command
     *
     * @param context     Command invocation context
     * @param commandName Command name
     */
    protected void showCommandDescription(CommandInvocationContext context, String commandName) {
        ConsoleParallax consoleParallax = context.getConsoleParallax();
        Optional<BaseCommand> optionalCommand = consoleParallax.getCommand(commandName);

        if (!optionalCommand.isPresent()) {
            consoleParallax.getOutputHandler().error("Command not found: " + commandName);
            return;
        }

        BaseCommand command = optionalCommand.get();

        consoleParallax.getOutputHandler().info("Command: " + command.getName());
        consoleParallax.getOutputHandler().info("Description: " + command.getDescription());
        consoleParallax.getOutputHandler().info("Syntax: " + command.getSyntax());
    }
}
