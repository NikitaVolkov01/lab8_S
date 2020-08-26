package command.commands;

import command.Command;
import exchange.Response;
import runner.Editor;

import java.util.ArrayList;
import java.util.List;

public final class HistoryCommand implements Command {
    @Override
    public Response execute(Editor editor) {
        List<String> response = new ArrayList<>();
        if (editor.getCommandHistory().getCommands().size() > 0) {
            response.add("История команд: ");
            editor.getCommandHistory().getCommands().stream()
                    .skip(Math.max(0, editor.getCommandHistory().getCommands().size() - 13))
                    .forEach(response::add);
            return new Response(true, response);
        }
        return new Response(false, "История команд отсутствует.");
    }
}