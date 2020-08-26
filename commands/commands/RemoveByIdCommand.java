package command.commands;

import command.Command;
import db.DataBaseConnector;
import exchange.Response;
import runner.Editor;
import runner.ExecutorException;
import ticket.TicketStorage;

import java.util.ArrayList;
import java.util.List;

public class RemoveByIdCommand implements Command {
    @Override
    public Response execute(Editor editor) throws ExecutorException {
        if (editor.getValue() == null) {
            throw new ExecutorException("Не введен ключ.");
        }
        if (TicketStorage.getTickets().size() > 0) {
            try {
                //System.out.println(editor.getValue());
                int key = Integer.parseInt(editor.getValue().trim());
                //DataBaseConnector.removeTicket(TicketStorage.);
                if (TicketStorage.removeTicket(key)){
                    return new Response(true, "Элемент по ключу " + key + " удален");
                }
                return new Response(true, "Элемент по ключу " + key + " не найден или не принадлежит вам");

                //TicketStorage.getTickets().stream().filter(el -> el.getId() == key).forEach(TicketStorage::removeTicket(el));



            } catch (NumberFormatException e) {
                throw new ExecutorException("Неверый формат ключа: " + editor.getValue(), e);
            }
        }
        return new Response(false, "В коллекции нет элементов.");
    }
}
