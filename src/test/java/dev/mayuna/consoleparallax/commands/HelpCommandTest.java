package dev.mayuna.consoleparallax.commands;

import dev.mayuna.consoleparallax.CommandInvocationContext;
import dev.mayuna.consoleparallax.ConsoleParallax;
import dev.mayuna.consoleparallax.ConsoleParallaxTest;
import dev.mayuna.consoleparallax.impl.TestCommandParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HelpCommandTest {

    @Test
    public void test() {
        HelpCommand helpCommand = new HelpCommand();

        assertNotNull(helpCommand.getName(), "Command name is null");
        assertEquals("help", helpCommand.getName(), "Command name is incorrect");

        assertNotNull(helpCommand.getDescription(), "Command description is null");
        assertNotNull(helpCommand.getUsage(), "Command usage is null");
        assertNotNull(helpCommand.getSyntax(), "Command parameters are null");

        ConsoleParallax consoleParallax = ConsoleParallaxTest.createTestBuilder().build();
        consoleParallax.registerDefaultHelpCommand();

        CommandInvocationContext onlyHelp = new CommandInvocationContext(consoleParallax, new TestCommandParser().parseCommand("help"));
        CommandInvocationContext helpWithArguments = new CommandInvocationContext(consoleParallax, new TestCommandParser().parseCommand("help help"));
        CommandInvocationContext helpWithUnknownArguments = new CommandInvocationContext(consoleParallax, new TestCommandParser().parseCommand("help unknown-command"));

        helpCommand.execute(onlyHelp);
        helpCommand.execute(helpWithArguments);
        helpCommand.execute(helpWithUnknownArguments);
    }
}
