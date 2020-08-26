package command;

import java.util.ArrayList;
import java.util.List;

public final class CommandsHistory {
    private int maxSize;
    private List<String> commands;
    public CommandsHistory() {
        maxSize = 100;
        commands = new ArrayList<>();
    }
    public CommandsHistory(int maxNumber) {
        this.maxSize = maxNumber;
        commands = new ArrayList<>();
    }
    public List<String> getCommands() {
        return commands;
    }
    public void addCommand(String command) {
        if (!command.trim().equals("show")){
            if (commands.size() > maxSize) {
                commands.remove(0);
            }
            commands.add(command);
        }
    }
}