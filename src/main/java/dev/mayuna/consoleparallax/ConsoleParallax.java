package dev.mayuna.consoleparallax;

import dev.mayuna.consoleparallax.commands.HelpCommand;
import dev.mayuna.consoleparallax.impl.ConsoleInputHandler;
import dev.mayuna.consoleparallax.impl.ConsoleOutputHandler;
import dev.mayuna.consoleparallax.impl.SimpleCommandParser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Main class for Console Parallax library
 */
@SuppressWarnings("FieldMayBeFinal")
@Getter
public class ConsoleParallax {

    protected final InputHandler inputHandler;
    protected final OutputHandler outputHandler;
    protected final Executor commandExecutor;
    protected final CommandParser commandParser;
    protected final List<BaseCommand> registeredCommands = new LinkedList<>();
    protected final Object mutex = new Object();
    protected boolean running;
    protected final @Getter(AccessLevel.NONE) Thread commandReader = createCommandReaderThread();

    /**
     * Creates a new ConsoleParallax instance
     *
     * @param inputHandler    Input handler
     * @param outputHandler   Output handler
     * @param commandParser   Command parser
     * @param commandExecutor Command executor
     */
    public ConsoleParallax(@NotNull @NonNull InputHandler inputHandler, @NotNull @NonNull OutputHandler outputHandler, @NotNull @NonNull CommandParser commandParser, @NotNull @NonNull Executor commandExecutor) {
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
        this.commandParser = commandParser;
        this.commandExecutor = commandExecutor;
    }

    /**
     * Creates a {@link ConsoleParallax} Builder with default values
     *
     * @return Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Starts the ConsoleParallax instance
     */
    public void start() {
        running = true;
        commandReader.start();
    }

    /**
     * Interrupts the command reader thread and stops the ConsoleParallax instance
     */
    public void interrupt() {
        running = false;
        commandReader.interrupt();
    }

    /**
     * Registers a command<br>
     * Replaces the command if it is already registered
     *
     * @param command Non-null command to register
     *
     * @return {@code true} if the command was successfully registered, {@code false} otherwise
     */
    public boolean registerCommand(@NotNull @NonNull BaseCommand command) {
        synchronized (mutex) {
            registeredCommands.removeIf(c -> c.getName().equalsIgnoreCase(command.getName()));
            return registeredCommands.add(command);
        }
    }

    /**
     * Registers the default help command
     */
    public void registerDefaultHelpCommand() {
        registerCommand(new HelpCommand());
    }

    /**
     * Unregisters a command
     *
     * @param command Non-null command to unregister
     *
     * @return {@code true} if the command was successfully unregistered, {@code false} otherwise
     */
    public boolean unregisterCommand(@NotNull @NonNull BaseCommand command) {
        synchronized (mutex) {
            return registeredCommands.remove(command);
        }
    }

    /**
     * Unregisters a command by name
     *
     * @param commandName Non-null command name
     *
     * @return {@code true} if the command was successfully unregistered, {@code false} otherwise
     */
    public boolean unregisterCommand(@NotNull @NonNull String commandName) {
        synchronized (mutex) {
            return registeredCommands.removeIf(c -> c.getName().equalsIgnoreCase(commandName));
        }
    }

    /**
     * Returns a list of registered commands
     *
     * @return Unmodifiable list of registered commands
     **/
    public List<BaseCommand> getRegisteredCommands() {
        return Collections.unmodifiableList(registeredCommands);
    }

    /**
     * Returns a command by name
     *
     * @param commandName Command name
     *
     * @return Command if found, {@link Optional#empty()} otherwise
     */
    public Optional<BaseCommand> getCommand(@NotNull @NonNull String commandName) {
        synchronized (mutex) {
            return registeredCommands.stream()
                                     .filter(command -> command.getName().equalsIgnoreCase(commandName))
                                     .findFirst();
        }
    }

    /**
     * Processes a command
     *
     * @param input Command input
     */
    protected void processCommand(@NotNull @NonNull String input) {
        CommandParseResult result = commandParser.parseCommand(input);

        synchronized (mutex) {
            Optional<BaseCommand> optionalCommand = getCommand(result.getCommandName());

            if (!optionalCommand.isPresent()) {
                outputHandler.error("Command not found: " + result.getCommandName());
                return;
            }

            BaseCommand command = optionalCommand.get();
            command.execute(new CommandInvocationContext(this, result));
        }

    }

    /**
     * Creates a new thread for reading commands
     *
     * @return Command reader thread
     */
    protected Thread createCommandReaderThread() {
        return new Thread(() -> {
            Thread.currentThread().setName("ConsoleParallax Command Reader");

            while (running) {
                String input = inputHandler.getNextInput();

                if (input == null || input.isEmpty()) {
                    continue;
                }

                commandExecutor.execute(() -> processCommand(input));
            }
        });
    }

    /**
     * Builder for {@link ConsoleParallax}
     */
    public static class Builder {

        protected InputHandler inputHandler = new ConsoleInputHandler();
        protected OutputHandler outputHandler = new ConsoleOutputHandler();
        protected Executor commandExecutor = Executors.newSingleThreadExecutor();
        protected CommandParser commandParser = new SimpleCommandParser();

        /**
         * Creates a new Builder instance with default values
         */
        protected Builder() {
        }

        /**
         * Sets the input handler
         *
         * @param inputHandler Input handler
         *
         * @return Builder instance
         */
        public Builder setInputHandler(@NotNull @NonNull InputHandler inputHandler) {
            this.inputHandler = inputHandler;
            return this;
        }

        /**
         * Sets the output handler
         *
         * @param outputHandler Output handler
         *
         * @return Builder instance
         */
        public Builder setOutputHandler(@NotNull @NonNull OutputHandler outputHandler) {
            this.outputHandler = outputHandler;
            return this;
        }

        /**
         * Sets the command executor
         *
         * @param commandExecutor Command executor
         *
         * @return Builder instance
         */
        public Builder setCommandExecutor(@NotNull @NonNull Executor commandExecutor) {
            this.commandExecutor = commandExecutor;
            return this;
        }

        /**
         * Sets the command parser
         *
         * @param commandParser Command parser
         *
         * @return Builder instance
         */
        public Builder setCommandParser(@NotNull @NonNull CommandParser commandParser) {
            this.commandParser = commandParser;
            return this;
        }

        /**
         * Builds a new instance of {@link ConsoleParallax}
         *
         * @return ConsoleParallax instance
         */
        public ConsoleParallax build() {
            return new ConsoleParallax(inputHandler, outputHandler, commandParser, commandExecutor);
        }
    }
}
