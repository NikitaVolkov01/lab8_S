package command.commands;

import adapters.CoordinatesDeserializer;
import adapters.CoordinatesSerializer;
import adapters.LocalDateDeserializer;
import adapters.LocalDateSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import command.Command;
import exchange.Response;
import runner.Editor;
import runner.ExecutorException;
import ticket.Coordinates;
import ticket.TicketStorage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;

public final class SaveCommand implements Command {
    @Override
    public Response execute(Editor editor) throws ExecutorException {
        if (TicketStorage.getTickets().size() > 0) {
            File sf = new File(editor.getDataFilePath());
            if (sf.isDirectory()) {
                throw new ExecutorException("Неверный путь к файлу.");
            }
            if (!sf.exists()) {
                try {
                    if (!sf.createNewFile()) {
                        throw new ExecutorException("Ошбика создания файла: ");
                    }
                } catch (IOException e) {
                    throw new ExecutorException("Ошбика создания файла: ", e);
                }
            }
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = new FileOutputStream(sf);
            } catch (FileNotFoundException e) {
                throw new ExecutorException("Неверный путь к файлу", e);
            }
            GsonBuilder gsonBuilder =
                    new GsonBuilder()
                            .setPrettyPrinting()
                            .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                            .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                            .registerTypeAdapter(Coordinates.class, new CoordinatesSerializer())
                            .registerTypeAdapter(Coordinates.class, new CoordinatesDeserializer());
            Gson gson = gsonBuilder.create();
            String json = gson.toJson(TicketStorage.getTickets());
            try {
                fileOutputStream.write(json.getBytes());
            } catch (IOException e) {
                throw new ExecutorException("Ошибка записи данных.", e);
            }
            return new Response(true, "Сохранение успешно произведено.");
        }
        return new Response(false, "В коллекции нет элементов.");
    }
}