# Console Parallax

![Coverage](.github/badges/jacoco.svg)

A simple Java library handling console input and treating it as a CLI.

It is designed to be quickly used, without any fuss and large amount of code or reading.

With the magic of annotations and without. ***It is up to you.***

> NOTE: Annotations are not yet implemented.

## Contents

- [Features](#features)
- [Quick showcase](#quick-showcase)
- [Installation](#installation)
  - [Gradle](#gradle)
  - [Maven](#maven)
- [Documentation](#documentation)

## Features

- Handling of console (or any other) input as a CLI
- Registering commands

## Quick showcase

```java
// Usually when program starts
public static void main(String[] args) {
    ConsoleParallax consoleParallax = new ConsoleParallax.Builder().build();
    consoleParallax.registerCommand(new ExampleCommand());
    consoleParallax.start();
    
    // You may now type "example <arg1>" into the console,
    // and it will print "Heya: <arg1>"
}

// Example command
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
```

## Installation
- Java >= 8

### Gradle

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'dev.mayuna:console-parallax:1.0.0'
}
```

### Maven

```xml
<dependency>
    <groupId>dev.mayuna</groupId>
    <artifactId>console-parallax</artifactId>
    <version>1.0.0</version>
</dependency>
```

**You can find the latest version [here](https://mvnrepository.com/artifact/dev.mayuna/console-parallax).**

## Documentation

### `ConsoleParallax` class

This class is the main class of the library. Calling `#start()` will start the command reader thread.

The command reader thread calls `InputHandler#getNextInput()` to get the next input. The default implementation
of `ConsoleInputHandler` calls `System.console().readLine()`.

All commands are executed on `ConsoleParallax`'s command executor.

You may create the `ConsoleParallax` instance with a (long) constructor or with its `Builder`:

  ```java
ConsoleParallax consoleParallax = ConsoleParallax.builder()
        .setInputHandler(/* input handler */)
        .setOutputHandler(/* output handler */)
        .setCommandParser(/* command parser */)
        .setCommandExecutor(/* command executor */)
        .build();

// The default implementation looks like this:
ConsoleParallax consoleParallax = ConsoleParallax.builder()
        .setInputHandler(new ConsoleInputHandler())
        .setOutputHandler(new ConsoleOutputHandler())
        .setCommandParser(new SimpleCommandParser())
        .setCommandExecutor(Executors.newSingleThreadExecutor())
        .build();
```

Default implementations (click to see):

- [`ConsoleInputHandler`](src/main/java/dev/mayuna/consoleparallax/impl/ConsoleInputHandler.java)
- [`ConsoleOutputHandler`](src/main/java/dev/mayuna/consoleparallax/impl/ConsoleOutputHandler.java)
- [`SimpleCommandParser`](src/main/java/dev/mayuna/consoleparallax/impl/SimpleCommandParser.java)

### Custom implementations

You may create your own implementations of `InputHandler`, `OutputHandler` and even `CommandParser`.
Please, see existing implementations for reference.

#### Pro tip: Logging

Your application may use different type of logging than just printing to the console. You can create your own
implementation of `OutputHandler` and override the two methods in there - `#info()` and `#error()`.

### Help command

[There's pre-implemented help command](src/main/java/dev/mayuna/consoleparallax/commands/HelpCommand.java)
that
shows you all registered commands. You can register it by calling `ConsoleParallax#registerDefaultHelpCommand()`.

I also recommend using this command as a reference.

### Custom commands

As seen in the quick showcase, all commands are just declared classes that implement `BaseCommand` interface.

There are several methods:

- `#getName()` - returns the name of the command, should be lowercase, without spaces
- `#execute(CommandInvocationContext context)` - the method that is called when the command is executed
- `#getUsage()` - returns short description of the command, used in the help command
- `#getSyntax()` - returns command's syntax, e.g., `example <arg1> [arg2]`
- `#getDescription()` - returns long description of the command, used in the help command

[Here you can find pre-implemented Help command as a reference](src/main/java/dev/mayuna/consoleparallax/commands/HelpCommand.java)

### `CommandInvocationContext` class

This class holds some information about the command's invocation:

- `#getConsoleParallax()` - returns the instance of `ConsoleParallax` that called the command
- `#getCommandName()` - returns the command's name that was specified in the input (
  calls `CommandParseResult#getCommandName()` internally)
- `#getArguments()` - returns the arguments that were specified in the input (calls `CommandParseResult#getArguments()`
  internally)
- `#getCommandParseResult()` - returns the result of the command parsing (`CommandParseResult`)

## Future plans

- Annotations (`@Command`, `@Argument`, etc.) with type checks
- Advanced command parser (`--help`, `--name="some-value"`, greedy strings, etc.)