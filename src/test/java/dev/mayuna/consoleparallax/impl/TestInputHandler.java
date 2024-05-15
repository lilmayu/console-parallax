package dev.mayuna.consoleparallax.impl;

import dev.mayuna.consoleparallax.InputHandler;
import lombok.SneakyThrows;

public class TestInputHandler implements InputHandler {

    private String command;
    private final Object mutex = new Object();

    public TestInputHandler() {
    }

    public void run(String command) {
        synchronized (mutex) {
            this.command = command;
            mutex.notifyAll();
        }
    }

    @SneakyThrows
    @Override
    public String getNextInput() {
        synchronized (mutex) {
            try {
                mutex.wait();
                return command;
            } catch (InterruptedException e) {
                return null;
            }
        }
    }
}
