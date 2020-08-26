package command.commands;

import command.Command;
import exchange.Response;
import runner.Editor;
import ticket.TicketStorage;

import java.util.ArrayList;
import java.util.List;

public final class InfoCommand implements Command {
    @Override
    public Response execute(Editor editor) {
        List<String> response = new ArrayList<>();
        response.add("Тип коллекции: " + TicketStorage.getTickets().getClass());
        response.add("Количество элементов: " + TicketStorage.getTickets().size());
        response.add("Дата инициализации: " + TicketStorage.getCreationDate());
        return new Response(true, response);
    }
}
