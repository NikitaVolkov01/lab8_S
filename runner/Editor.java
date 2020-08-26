package runner;


import adapters.CoordinatesDeserializer;
import adapters.CoordinatesSerializer;
import adapters.LocalDateDeserializer;
import adapters.LocalDateSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import command.CommandsHistory;
import exchange.Response;
import io.Input;
import io.Output;
import ticket.Coordinates;
import ticket.Ticket;
import ticket.TicketCreator;
import ticket.TicketStorage;

import javax.xml.bind.ValidationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

/**
 * Editor class contains data to be passed to command
 */
public final class Editor {
    private Input in;
    private Output out;
    private Ticket ticket;
    private String value;
    private boolean running;
    private boolean fromFile;
    private String dataFilePath;
    private ArrayList<Ticket> collection;
    private List<Integer> filesHashes;
    private CommandsHistory commandsHistory;
    private Date date;

    public Editor() {
        value = null;
        running = true;
        fromFile = false;
        in = new Input();
        out = new Output();
        collection = null;
        filesHashes = new ArrayList<>();
        commandsHistory = new CommandsHistory();
    }

    public String getDataFilePath() {
        return dataFilePath;
    }

    /**
     * Sets data file path
     *
     * @param dataFilePath data file path
     */
    public void setDataFilePath(String dataFilePath) {
        this.dataFilePath = dataFilePath;
    }

    /**
     * Метод, отвечающий за заполнение коллекции из файла.
     *
     * @return Response about correctness
     */
    public Response load() {
        try {
            File file = new File(dataFilePath);
            Scanner fileScanner = new Scanner(new FileInputStream(file));
            fileScanner.useDelimiter("\\Z");
            String data = fileScanner.next();
            Gson gson =
                    new GsonBuilder()
                            .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                            .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                            .registerTypeAdapter(Coordinates.class, new CoordinatesSerializer())
                            .registerTypeAdapter(Coordinates.class, new CoordinatesDeserializer())
                            .create();
            //setCollection(gson.fromJson(data, ArrayList.class));
            TicketStorage.setTickets(gson.fromJson(data, ArrayList.class));
        } catch (IOException e) {
            return new Response(false, "Ошибка чтения данных");
        } catch (NoSuchElementException | JsonSyntaxException | ValidationException e) {
            return new Response(false, "Неверный формат данных");
        }
        return new Response(true, "Файл загружен");
    }

    public Ticket getTicket() throws ValidationException {
        if (fromFile) {
            ticket = new TicketCreator(in, out, collection).create();
        }
        ticket.setId(TicketStorage.getTickets().size() + 1);
        ticket.setCreationDate(LocalDate.now());
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    /**
     * Returns true if read is from file
     *
     * @return True if read is from file
     */
    public boolean getFromFile() {
        return fromFile;
    }

    /**
     * Sets if read is from file
     *
     * @param fromFile true if from file
     */
    public void setFromFile(boolean fromFile) {
        this.fromFile = fromFile;
    }

    /**
     * Returns entered value
     *
     * @return Entered value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value entered
     *
     * @param value New entered value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Returns files hashes to catch recursion
     *
     * @return Files hashes
     */
    public List<Integer> getFilesHashes() {
        return filesHashes;
    }

    /**
     * Returns input
     *
     * @return Input
     */
    public Input getIn() {
        return in;
    }

    /**
     * Sets input
     *
     * @param in new input
     */
    public void setIn(Input in) {
        this.in = in;
    }

    /**
     * Returns output
     *
     * @return Output
     */
    public Output getOut() {
        return out;
    }

    /**
     * Sets output
     *
     * @param out new output
     */
    public void setOut(Output out) {
        this.out = out;
    }

    /**
     * Clears files hashes list
     */
    public void clearFilesHashes() {
        filesHashes.clear();
    }

    /**
     * Returns running parameter Program stops if running parameter become false
     *
     * @return Running parameter
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Sets running parameter
     *
     * @param running new running parameter value
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * Returns collection to work with
     *
     * @return Collection to work with
     */
    public ArrayList<Ticket> getCollection() {
        return collection;
    }
    /*public String printCollection(){
        String res = "";
        //collection.stream().forEach(m -> res += m.toString());
        for (Ticket t: collection){
            res += t.toString();
        }
        return collection.;
    }*/

    private void setCollection(ArrayList collection) {
        this.collection = collection;
        date = new Date();
    }

    /**
     * Returns history of correctly executed commands
     *
     * @return History of correctly executed commands
     */
    public CommandsHistory getCommandHistory() {
        return commandsHistory;
    }
    public Date getDate(){return date;}
}
