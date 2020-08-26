package command.commands;


import command.Command;
import db.DataBaseConnector;
import exchange.Response;
import runner.Editor;
import ticket.TicketStorage;

public final class ClearCommand implements Command {
    @Override
    public Response execute(Editor editor) {
        if (TicketStorage.getTickets().size() > 0) {
            TicketStorage.getTickets().stream().forEach(ticket -> DataBaseConnector.removeTicket(ticket, DataBaseConnector.getLogin()));
            //DataBaseConnector.deleteAll();
            TicketStorage.clear();
            return new Response(true, "Коллекция очищена");
        }
        return new Response(false, "В коллекции нет элементов");
    }
}