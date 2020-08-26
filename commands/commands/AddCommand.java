package command.commands;

import command.Command;
import db.DataBaseConnector;
import exchange.Request;
import exchange.Response;
import ticket.Ticket;
import runner.Editor;
import runner.ExecutorException;

import java.util.ArrayList;
import java.util.List;

public final class AddCommand implements Command {
    private Ticket ticket;
    public void addRequest(Request request){
        this.ticket = request.getTicket();
    }
    @Override
    public Response execute(Editor editor) throws ExecutorException {
        List<String> response = new ArrayList<>();
        response.add("Добавление нового элемента прошло успешно.");
        System.out.println("TICKET " + ticket);

        

        int currT = 0;
        for (Ticket t: DataBaseConnector.readTicket()){
            if (t.getId()>currT){currT = t.getId();}
        }

        response.add(String.valueOf(currT));

        response.add(String.valueOf(DataBaseConnector.getAddEventId()));
        System.out.println("EVENT ID >>> " + DataBaseConnector.getAddEventId());

        return new Response(true, response);
    }
}
