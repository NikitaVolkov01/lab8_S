package ticket;

import adapters.CoordinatesDeserializer;
import adapters.CoordinatesSerializer;
import adapters.LocalDateDeserializer;
import adapters.LocalDateSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import db.DataBaseConnector;

import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.util.*;

public class TicketStorage {
    static {
        creationDate = LocalDate.now();
    }
    private static Gson gson =
            new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                    .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                    .registerTypeAdapter(Coordinates.class, new CoordinatesSerializer())
                    .registerTypeAdapter(Coordinates.class, new CoordinatesDeserializer())
            .create();
    private static LocalDate creationDate;
    private static TreeSet<Ticket> set = new TreeSet<Ticket>();
    public static boolean putTicket(Ticket ticket){
        boolean s = set.add(ticket);
        return s;
    }



    public static void setTickets(ArrayList fromJson) throws ValidationException {

    }

    public static TreeSet<Ticket> getTickets(){
        return set;
    }
    public static boolean removeTicket(int id){
        Optional<Ticket> t = TicketStorage.getTickets().stream().filter(el -> el.getId() == id).findFirst();
        if (t.isPresent()){
            Ticket ticket = t.get();
            if (!DataBaseConnector.removeTicket(ticket, DataBaseConnector.getLogin())){
                return false;
            }
            set.remove(ticket);
            return true;
        }
        else return false;
    }
    public static void clear(){
        Iterator i = set.iterator();
        while (i.hasNext()) {
            synchronized (i) {
                Ticket ticket = (Ticket) i.next();
                if (ticket.getAuthor().equals(DataBaseConnector.getLogin())) {
                    i.remove();
                }
            }
        }
    }
    public static LocalDate getCreationDate(){
        return creationDate;
    }
    public static Ticket decriptTicket(String s){
        s = s.replaceAll("\"", "").replaceAll("bestEvent", "event").replaceAll("number", "ticketsCount").replaceAll("ticket=", "").replaceAll("cost", "price").replaceAll("event=", "s");
        String[] pts = s.split(",");
        String name = "";
        String eventname = "";
        long y = 0;
        float x = 0;
        Long price = null;
        TicketType tt = null;
        Integer eventid = null;
        Long eventCount = null;
        boolean namec = true;
        for (String pt: pts){
            if (pt.contains("name=") && namec){
                //System.out.println(pt);
                name = pt.split("=")[1];
                namec = false;
            }
            if (pt.contains("name=") && !namec){
                eventname = pt.split("=")[1];
            }
            if (pt.contains("x=")){
                x = Float.parseFloat(pt.split("=")[2]);
            }
            if (pt.contains("y=")){
                y = Long.parseLong(pt.replace(".0", "").replace("}", "").split("=")[1]);
            }
            if (pt.contains("price=")){
                price = Long.parseLong(pt.replace(".0", "").replaceAll("}", "").split("=")[1]);
            }
            if (pt.contains("type=")){

                tt = TicketType.valueOf(pt.replaceAll("}", "").trim().split("=")[1]);
            }
            if (pt.contains("ticketType=")){
                tt = TicketType.valueOf(pt.replaceAll("}", "").trim().split("=")[1]);
            }
            if (pt.contains("id=")){

                eventid = Integer.parseInt(pt.replace(".0", "").replaceAll("}", "").split("=")[1]);
            }
            if (pt.contains("ticketsCount=")){
                eventCount = Long.parseLong(pt.replace(".0", "").replaceAll("}", "").split("=")[1]);
            }
        }
        try {
            Coordinates coordinates = new Coordinates(y, x);
            Ticket ticket;
            try {
                try {
                    Event e = TicketStorage.getTickets().stream().filter(el -> el.hasEvent()).max((a, b) -> (Integer.compare((int)a.getEvent().getId(), (int)b.getEvent().getId()))).get().getEvent();
                    if (e == null) eventid = (int)e.getId();
                    else eventid = (int)e.getId() + 1;
                }catch (NoSuchElementException e){
                    eventid = 1;
                }

                Event event = new Event(eventname, eventCount);
                event.setId(eventid);

                ticket = new Ticket(name, new Coordinates(y, x), price, tt, event);
            }catch (ValidationException | NullPointerException e){
                //e.printStackTrace();
                ticket = new Ticket(name, new Coordinates(y, x), price, tt, null);
            }
            ticket.setId(TicketStorage.getTickets().size()+1);
            ticket.setCreationDate(LocalDate.now());
            return ticket;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

}
