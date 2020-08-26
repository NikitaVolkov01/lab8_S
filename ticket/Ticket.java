package ticket;

import javax.xml.bind.ValidationException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Ticket implements Comparable<Ticket>, Serializable {
    private Integer id;
    private String name;
    private Coordinates coordinates;
    private LocalDate creationDate;
    private long cost;
    private TicketType ticketType;
    private Event event = null;
    private String author;
    public Ticket(
            String name,
            Coordinates coordinates,
            long cost,
            TicketType type,
            Event event)
            throws ValidationException {
        setName(name);
        setCoordinates(coordinates);
        setCost(cost);
        setType(type);
        setevent(event);
    }
    public Ticket() {
    }
    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) throws ValidationException {
        if (id == null) {
            throw new ValidationException("Значение id не может быть null.");
        }
        if (id <= 0) {
            throw new ValidationException("Значение id должно быть больше 0.");
        }
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) throws ValidationException {
        if (name == null) {
            throw new ValidationException("Имя не может быть null.");
        }
        if (name.equals("")) {
            throw new ValidationException("Имя не может быть пустой строкой.");
        }
        this.name = name;
    }
    public Coordinates getCoordinates() {
        return this.coordinates;
    }
    public void setCoordinates(Coordinates coordinates) throws ValidationException {
        if (coordinates == null) {
            throw new ValidationException("Значение coordinates не может быть null.");
        }
        this.coordinates = coordinates;
    }
    public LocalDate getCreationDate() {
        return this.creationDate;
    }
    public void setCreationDate(LocalDate creationDate) throws ValidationException {
        if (creationDate == null) {
            throw new ValidationException("Значение createDate не може быть null.");
        }

        this.creationDate = creationDate;
    }
    public long getcost() {
        return this.cost;
    }
    public void setCost(long cost) throws ValidationException {
        if (cost <= 0) {
            throw new ValidationException("Значение цены должно быть больше 0.");
        }

        this.cost = cost;
    }
    public TicketType getType() {
        return this.ticketType;
    }
    public void setType(TicketType t) {
        this.ticketType = t;
    }
    public Event getEvent() {
        return this.event;
    }
    public void setevent(Event event) {
        this.event = event;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, coordinates, creationDate, cost, ticketType, event);
    }
    @Override
    public int compareTo(Ticket other) {
        return (this.name.length() + (int)this.cost + Math.round(this.getCoordinates().getX()) - other.getName().length() - (int)other.cost - Math.round(other.getCoordinates().getX()) + this.id - other.id );
    }
    public boolean hasEvent(){
        return !(this.event==null);
    }
    @Override
    public String toString() {
        String ticket = "";
        ticket += "ID: " + this.id + ", название: " + this.name + ", координаты: (" + this.coordinates.getX() + ';' + this.coordinates.getY() + "), дата создания: "
                + this.creationDate + ", цена: " + this.cost;
        //System.out.println("ATTENTION: >>> " + this.ticketType);
        if (!(this.ticketType == null)){
            ticket += ", тип " + this.ticketType.toString();
        }
        if (!(this.event == null)){
            ticket += ", событие " + this.event.getName();
            ticket += ", количество билетов " + this.event.getNumber();
            ticket += ", event id " + this.event.getId();
        }
        ticket += ", автор " + this.author;
        return ticket;

        /*if (this.event != null && this.ticketType != null) {
            return ("ID: "
                    + this.id
                    + ", название: "
                    + this.name
                    + ", координаты: ("
                    + this.coordinates.getX()
                    + ';'
                    + this.coordinates.getY()
                    + "), дата создания: "
                    + this.creationDate
                    + ", цена: "
                    + this.cost
                    + ", тип: "
                    + this.ticketType.toString()
                    + ", событие: "
                    + this.event.getName()
                    + " (количество билетов: "
                    + this.event.getTracks()
                    + ", id: "
                    + this.event.getNumber()
                    + ").");
        } else {
            if (this.event == null && this.ticketType != null) {
                return ("ID: "
                        + this.id
                        + ", название: "
                        + this.name
                        + ", координаты: ("
                        + this.coordinates.getX()
                        + ';'
                        + this.coordinates.getY()
                        + "), дата создания: "
                        + this.creationDate
                        + ", количество участников: "
                        + this.cost
                        + ", жанр: "
                        + this.ticketType.toString()
                        + ", лучший альбом: неизвестен.");
            } else {
                if (this.event != null && this.ticketType == null) {
                    return ("ID: "
                            + this.id
                            + ", название исполнителя: "
                            + this.name
                            + ", координаты: ("
                            + this.coordinates.getX()
                            + ';'
                            + this.coordinates.getY()
                            + "), дата создания: "
                            + this.creationDate
                            + ", цена: "
                            + this.cost
                            + ", событие: "
                            + this.event.getName()
                            + " (количество треков: "
                            + this.event.getTracks()
                            + ", продажи: "
                            + this.event.getNumber()
                            + ").");
                } else {
                    return ("ID: "
                            + this.id
                            + ", название исполнителя: "
                            + this.name
                            + ", координаты: ("
                            + this.coordinates.getX()
                            + ';'
                            + this.coordinates.getY()
                            + "), дата создания: "
                            + this.creationDate
                            + ", количество участников: "
                            + this.cost
                            + ", жанр: неизвестен, лучший альбом: неизвестен.");
                }
            }
        }*/
    }
}