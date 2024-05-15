package dev.mayuna.consoleparallax;

import dev.mayuna.consoleparallax.impl.SimpleCommandParser;
import dev.mayuna.consoleparallax.impl.TestCommandParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("DataFlowIssue")
public class CommandInvocationContextTest {

    @Test
    public void test() {
        ConsoleParallax consoleParallax = ConsoleParallaxTest.createTestBuilder().build();
        TestCommandParser commandParser = new TestCommandParser();
        CommandParseResult result = commandParser.parseCommand("test abc");

        assertThrows(NullPointerException.class, () -> new CommandInvocationContext(null, null), "Null ConsoleParallax and CommandParseResult should throw NullPointerException");
        assertThrows(NullPointerException.class, () -> new CommandInvocationContext(consoleParallax, null), "Null CommandParseResult should throw NullPointerException");
        assertThrows(NullPointerException.class, () -> new CommandInvocationContext(null, commandParser.parseCommand("test")), "Null ConsoleParallax should throw NullPointerException");

        CommandInvocationContext context = new CommandInvocationContext(consoleParallax, result);

        assertEquals("test", context.getCommandName(), "Command name is incorrect");
        assertEquals("abc", context.getArguments()[0], "Arguments are incorrect");

        assertSame(consoleParallax, context.getConsoleParallax(), "ConsoleParallax instance is incorrect");
        assertSame(result, context.getCommandParseResult(), "CommandParseResult instance is incorrect");
    }
}
