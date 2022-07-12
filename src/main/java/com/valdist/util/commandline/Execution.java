package com.valdist.util.commandline;

public class Execution {
    public Execution(String command, String description, Runnable action) {
        this.command = command;
        this.description = description;
        this.action = action;
    }

    private String command;
    private String description;
    private Runnable action;

    public String getCommand(){
        return command;
    }

    public String getDescription(){
        return description;
    }

    public void execute(){
        action.run();
    }
}
