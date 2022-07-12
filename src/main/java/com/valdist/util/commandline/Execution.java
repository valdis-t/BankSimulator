package com.valdist.util.commandline;

public class Execution {
    public interface Action{
        void run();
    }

    public Execution(String command, String description, Action action) {
        this.command = command;
        this.description = description;
        this.action = action;
    }

    private String command;
    private String description;
    private Action action;

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
