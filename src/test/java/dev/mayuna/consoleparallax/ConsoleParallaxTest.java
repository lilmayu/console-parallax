package dev.mayuna.consoleparallax;

import dev.mayuna.consoleparallax.impl.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.Console;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("DataFlowIssue")
public class ConsoleParallaxTest {

    public static ConsoleParallax.Builder createTestBuilder() {
        return ConsoleParallax.builder()
                .setCommandParser(new TestCommandParser())
                .setInputHandler(new TestInputHandler())
                .setOutputHandler(new TestOutputHandler());
    }

    @Test
    public void testConstructor() {
        assertThrows(NullPointerException.class, () -> new ConsoleParallax(null, new TestOutputHandler(), new TestCommandParser(), Executors.newSingleThreadExecutor()), "Null passed to the constructor's inputHandler, should throw NullPointerException");
        assertThrows(NullPointerException.class, () -> new ConsoleParallax(new TestInputHandler(), null, new TestCommandParser(), Executors.newSingleThreadExecutor()), "Null passed to the constructor's outputHandler, should throw NullPointerException");
        assertThrows(NullPointerException.class, () -> new ConsoleParallax(new TestInputHandler(), new TestOutputHandler(), null, Executors.newSingleThreadExecutor()), "Null passed to the constructor's commandParser, should throw NullPointerException");
        assertThrows(NullPointerException.class, () -> new ConsoleParallax(new TestInputHandler(), new TestOutputHandler(), new TestCommandParser(), null), "Null passed to the constructor's commandExecutor, should throw NullPointerException");

        Executor executor = Executors.newSingleThreadExecutor();

        ConsoleParallax consoleParallax = new ConsoleParallax(new TestInputHandler(), new TestOutputHandler(), new TestCommandParser(), executor);

        assertNotNull(consoleParallax, "ConsoleParallax instance should not be null");
        assertEquals(executor, consoleParallax.getCommandExecutor(), "Executor should be the same as the one passed to the constructor");
        assertInstanceOf(TestInputHandler.class, consoleParallax.getInputHandler(), "Input handler should be an instance of TestInputHandler");
        assertInstanceOf(TestOutputHandler.class, consoleParallax.getOutputHandler(), "Output handler should be an instance of TestOutputHandler");
        assertInstanceOf(TestCommandParser.class, consoleParallax.getCommandParser(), "Command parser should be an instance of TestCommandParser");
        assertSame(consoleParallax.getCommandExecutor(), executor, "Executor should be the same as the one passed to the constructor");
    }

    @Test
    public void testBuilder() {
        ConsoleParallax.Builder builder = ConsoleParallax.builder();

        assertThrows(NullPointerException.class, () -> builder.setCommandExecutor(null), "Null passed to the builder's #setCommandExecutor(), should throw NullPointerException");
        assertThrows(NullPointerException.class, () -> builder.setCommandParser(null), "Null passed to the builder's #setCommandParser(), should throw NullPointerException");
        assertThrows(NullPointerException.class, () -> builder.setInputHandler(null), "Null passed to the builder's #setInputHandler(), should throw NullPointerException");
        assertThrows(NullPointerException.class, () -> builder.setOutputHandler(null), "Null passed to the builder's #setOutputHandler(), should throw NullPointerException");

        Executor executor = Executors.newSingleThreadExecutor();

        builder.setOutputHandler(new TestOutputHandler());
        builder.setInputHandler(new TestInputHandler());
        builder.setCommandParser(new TestCommandParser());
        builder.setCommandExecutor(executor);

        ConsoleParallax consoleParallax = builder.build();

        assertNotNull(consoleParallax, "ConsoleParallax instance should not be null");
        assertEquals(executor, consoleParallax.getCommandExecutor(), "Executor should be the same as the one passed to the builder");
        assertInstanceOf(TestInputHandler.class, consoleParallax.getInputHandler(), "Input handler should be an instance of TestInputHandler");
        assertInstanceOf(TestOutputHandler.class, consoleParallax.getOutputHandler(), "Output handler should be an instance of TestOutputHandler");
        assertInstanceOf(TestCommandParser.class, consoleParallax.getCommandParser(), "Command parser should be an instance of TestCommandParser");
        assertSame(consoleParallax.getCommandExecutor(), executor, "Executor should be the same as the one passed to the builder");
    }

    @Test
    public void testRunning() {
        ConsoleParallax consoleParallax = createTestBuilder().build();

        assertFalse(consoleParallax.isRunning(), "ConsoleParallax should not be running before start() is called");

        consoleParallax.start();

        assertTrue(consoleParallax.isRunning(), "ConsoleParallax should be running after start() is called");

        consoleParallax.interrupt();

        assertFalse(consoleParallax.isRunning(), "ConsoleParallax should not be running after interrupt() is called");
    }

    @Test
    public void testCommandRegistration() {
        ConsoleParallax consoleParallax = createTestBuilder().build();

        assertThrows(NullPointerException.class, () -> consoleParallax.registerCommand(null), "Null passed to #registerCommand(), should throw NullPointerException");

        consoleParallax.registerCommand(new TestCommand());

        assertThrows(NullPointerException.class, () -> consoleParallax.getCommand(null), "Null passed to #getCommand(), should throw NullPointerException");
        assertNotNull(consoleParallax.getCommand(TestCommand.NAME), "Command should not be null");
    }

    @Test
    public void testCommandReplacement() {
        ConsoleParallax consoleParallax = createTestBuilder().build();

        BaseCommand firstCommand = new TestCommand();
        BaseCommand secondCommand = new TestCommand();

        // Register the first command
        consoleParallax.registerCommand(firstCommand);

        assertTrue(consoleParallax.getCommand(TestCommand.NAME).isPresent(), "Command should not be null");
        assertSame(firstCommand, consoleParallax.getCommand(TestCommand.NAME).get(), "Command should be the first command");

        // Another command with the same name
        consoleParallax.registerCommand(secondCommand);

        assertTrue(consoleParallax.getCommand(TestCommand.NAME).isPresent(), "Command should not be null");
        assertSame(secondCommand, consoleParallax.getCommand(TestCommand.NAME).get(), "Command should be the second command");
    }

    @Test
    public void testDefaultHelpCommand() {
        ConsoleParallax consoleParallax = createTestBuilder().build();

        consoleParallax.registerDefaultHelpCommand();

        assertNotNull(consoleParallax.getCommand("help"), "Help command should not be null");
    }

    @Test
    public void testCommandUnregistration() {
        ConsoleParallax consoleParallax = createTestBuilder().build();

        assertThrows(NullPointerException.class, () -> consoleParallax.unregisterCommand((String)null), "Null passed to #unregisterCommand(), should throw NullPointerException");
        assertThrows(NullPointerException.class, () -> consoleParallax.unregisterCommand((BaseCommand) null), "Null passed to #unregisterCommand(), should throw NullPointerException");

        BaseCommand command = new TestCommand();

        consoleParallax.registerCommand(command);

        assertTrue(consoleParallax.getCommand(TestCommand.NAME).isPresent(), "Command should not be null");

        consoleParallax.unregisterCommand(command);

        assertFalse(consoleParallax.getCommand(TestCommand.NAME).isPresent(), "Command should be null");

        consoleParallax.registerCommand(command);

        assertTrue(consoleParallax.getCommand(TestCommand.NAME).isPresent(), "Command should not be null");

        consoleParallax.unregisterCommand(TestCommand.NAME);

        assertFalse(consoleParallax.getCommand(TestCommand.NAME).isPresent(), "Command should be null");
    }

    @Test
    public void testRegisteredCommandsGetter() {
        ConsoleParallax consoleParallax = createTestBuilder().build();

        BaseCommand command = new TestCommand();

        consoleParallax.registerCommand(command);

        assertEquals(1, consoleParallax.getRegisteredCommands().size(), "Registered commands should have a size of 1");
        assertSame(command, consoleParallax.getRegisteredCommands().get(0), "Command should be the same as the one registered");
    }

    @Test
    public void testProcessCommand() {
        ConsoleParallax consoleParallax = createTestBuilder().build();

        TestCommand command = new TestCommand();

        consoleParallax.registerCommand(command);

        assertThrows(NullPointerException.class, () -> consoleParallax.processCommand(null), "Null passed to #processCommand(), should throw NullPointerException");
        consoleParallax.processCommand("test");

        assertTrue(command.executed, "Command should have been executed");

        command.executed = false;

        consoleParallax.processCommand("some-other");

        assertFalse(command.executed, "Command should not have been executed");
    }

    @Test
    public void testCommandReaderThread() {
        ConsoleParallax.Builder consoleParallaxBuilder = createTestBuilder();
        TestInputHandler inputHandler = new TestInputHandler();
        consoleParallaxBuilder.setInputHandler(inputHandler);
        ConsoleParallax consoleParallax = consoleParallaxBuilder.build();

        TestCommand command = new TestCommand();

        consoleParallax.registerCommand(command);

        consoleParallax.start();

        assertDoesNotThrow(() -> Thread.sleep(10)); // Wait for command reader to start

        // Should do nothing
        inputHandler.run(null);
        inputHandler.run("");

        assertFalse(command.executed, "Command should not have been executed before execution");

        // Should execute the command
        inputHandler.run(TestCommand.NAME);

        assertDoesNotThrow(() -> Thread.sleep(10)); // Wait for the command to be executed

        assertTrue(command.executed, "Command should have been executed");

        consoleParallax.interrupt();

        assertFalse(consoleParallax.isRunning(), "ConsoleParallax should not be running after interrupt() is called");
    }

    @Test
    public void testMisc() {
        ConsoleParallax consoleParallax = createTestBuilder().build();

        assertNotNull(consoleParallax.getMutex(), "Mutex should not be null");
        assertNotNull(consoleParallax.commandReader, "Command reader should not be null");

        ConsoleOutputHandler outputHandler = new ConsoleOutputHandler();
        outputHandler.info("info");
        outputHandler.error("err");
    }
}
