package command.commands;

import command.Command;
import db.DataBaseConnector;
import exchange.Response;
import runner.Editor;
import ticket.TicketStorage;

import java.sql.SQLException;

public class LoginCommand implements Command {
    @Override
    public Response execute(Editor editor) {
        String login = editor.getValue().trim().split(" ")[0];
        String pass = editor.getValue().trim().split(" ")[1];
        try {
            if (DataBaseConnector.login(login, pass)) {
                DataBaseConnector.setLogin(login);
                return new Response(true, "Успешный логин");
            }else throw new SQLException();
        } catch (SQLException e) {
            e.printStackTrace();
            return new Response(false, "Неверный логин");
        }
    }
}
