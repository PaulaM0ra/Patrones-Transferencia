package com.losatuendos.alquilerapp.pattern.command;

import java.util.ArrayList;
import java.util.List;

public class SistemaInvoker {
    private OperacionCommand command;
    private final List<OperacionCommand> history = new ArrayList<>();

    public void setCommand(OperacionCommand command) {
        this.command = command;
    }

    public void executeCommand() {
        if (command != null) {
            history.add(command);
            command.execute();
        } else {
            System.out.println("No command set to execute.");
        }
    }

    public List<OperacionCommand> getHistory() {
        return history;
    }
}
