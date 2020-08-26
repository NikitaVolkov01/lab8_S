package command.commands;

import command.Command;
import exchange.Response;
import runner.Editor;
import ticket.TicketStorage;

import java.util.ArrayList;
import java.util.List;

public final class ShowCommand implements Command {
    @Override
    public Response execute(Editor editor) {
        List<String> response = new ArrayList<>();
        if (TicketStorage.getTickets().size() > 0) {
            response.add("Элементы коллекции в строковом представлении: ");
            TicketStorage.getTickets().stream().forEach(el -> response.add(el.toString()));
            System.out.println("\n\n\n\n\n RESPONSE \n\n\n\n " + response);
            return new Response(true, response);
        }
        return new Response(false, "В коллекции нет элементов.");
    }
}