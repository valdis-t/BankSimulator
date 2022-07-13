package com.valdist.util.commandline;

public class Execution {
    private final String command;
    private final String description;
    private final Runnable action;
    public Execution(String command, String description, Runnable action) {
        this.command = command;
        this.description = description;
        this.action = action;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public void execute() {
        action.run();
    }
}
