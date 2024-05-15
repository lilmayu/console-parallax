package dev.mayuna.consoleparallax;

import dev.mayuna.consoleparallax.impl.ConsoleInputHandler;
import dev.mayuna.consoleparallax.impl.ConsoleOutputHandler;
import dev.mayuna.consoleparallax.impl.SimpleCommandParser;

import java.util.concurrent.Executors;

public class Main {

    public static class ExampleCommand implements BaseCommand {

        @Override
        public String getName() {
            return "example";
        }

        @Override
        public void execute(CommandInvocationContext context) {
            System.out.println("Heya: " + context.getArguments()[0]);
        }
    }

    public static void main(String[] args) {
ConsoleParallax consoleParallax = new ConsoleParallax.Builder()
        .setInputHandler(new ConsoleInputHandler())
        .setOutputHandler(new ConsoleOutputHandler())
        .setCommandParser(new SimpleCommandParser())
        .setCommandExecutor(Executors.newSingleThreadExecutor())
        .build();

consoleParallax.registerDefaultHelpCommand();
    }

}
