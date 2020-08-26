import db.DataBaseConnector;
import ticket.TicketStorage;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.net.BindException;
import java.util.Scanner;

public class App {
    /**
     * Starts server
     *
     * @param args name of the file with data
     */
    public static void main(String[] args) throws IOException {
        try {
            System.out.print("Введите порт ");
            Scanner sc = new Scanner(System.in);
            String port = sc.nextLine();
            System.out.println("Введите логин для доступа к Базе Данных ");
            String login = sc.nextLine().trim();
            System.out.println("Введите пароль для доступа к Базе Данных ");
            String pass = sc.nextLine().trim();
            DataBaseConnector.setDBEnter(login, pass);
            Server server = new Server(Integer.parseInt(port));
                try {
                    TicketStorage.setTickets(DataBaseConnector.readTicket());
                }catch (NullPointerException e){
                    System.out.println("Логин & пароль для доступа к БД неверны");
                    System.exit(1);
                }catch ( ValidationException e){
                    System.out.println("База данных пуста");
                }
                System.out.println("Сервер успешно запущен.");
                while (server.isRunning()) {
                    if (server.isConnected()) {
                        server.process();
                    }
                }
                server.close();

        }catch (BindException e) {
            System.err.println("Порт уже занят");
        }catch (Throwable e) {
            e.printStackTrace();
            System.err.println("Фатальная ошибка при работе сервера: " + e.getMessage());
        }
    }
}