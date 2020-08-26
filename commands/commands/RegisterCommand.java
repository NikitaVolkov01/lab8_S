package command.commands;

import command.Command;
import db.DataBaseConnector;
import exchange.Response;
import runner.Editor;
import ticket.TicketStorage;

import java.sql.SQLException;

public class RegisterCommand implements Command {
    @Override
    public Response execute(Editor editor) {

        try {
        //System.out.println("VALUE   " + " " + editor.getValue().trim());
            String login = editor.getValue().trim().split(" ")[0];
            String pass = editor.getValue().trim().split(" ")[1];

            if (DataBaseConnector.register(login, pass)) {
                return new Response(true, "Успешная регистрация");
            }else throw new SQLException();
        } catch (SQLException | ArrayIndexOutOfBoundsException e) {
            //e.printStackTrace();
            return new Response(false, "Регистрацию выполнить не удалось");
        }
    }
}
