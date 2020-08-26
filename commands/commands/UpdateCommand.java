package command.commands;

import command.Command;
import db.DataBaseConnector;
import exchange.Request;
import exchange.Response;
import runner.Editor;
import runner.ExecutorException;
import ticket.Ticket;
import ticket.TicketStorage;

import javax.xml.bind.ValidationException;
import java.time.LocalDate;

public class UpdateCommand implements Command {

    private Request request;

    public void addRequest(Request req){
        request = req;
    }
    @Override
    public Response execute(Editor editor) throws ExecutorException {
        if (request.getTicket().getId() == null) {
            throw new ExecutorException("Не указан id.");
        }
        int id;
        try {
            id = request.getTicket().getId();
        } catch (NumberFormatException e) {
            throw new ExecutorException("Неверный формат id: " + editor.getValue(), e);
        }
        Ticket tick = TicketStorage.getTickets().stream().filter(el -> el.getId() == id).filter(el -> el.getAuthor().equals(DataBaseConnector.getLogin())).findFirst().orElse(null);
        if (tick == null) {
            throw new ExecutorException("Элемент с id: " + id + " не найден.");
        }
        try {
            tick.setCreationDate(LocalDate.now());
            tick.setCost(tick.getcost() + 10);
        } catch (ValidationException e) {
            e.printStackTrace();
        }

        System.out.println("GOING yo TICK UPDATING on DB " +
                tick.getcost() + " " + tick.getId());
        DataBaseConnector.updateTicket(tick);

        return new Response(true, "Элемент обновлен по id " + id);
    }
}
