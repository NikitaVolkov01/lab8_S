package ticket;

import javax.xml.bind.ValidationException;
import java.io.Serializable;

public class Event implements Serializable {
    private String name;
    //private int tracks;
    private float number;
    private int id;
    public Event(String name, int id, long number) throws ValidationException {
        setName(name);
        setId(id);
        setNumber(number);
    }
    public Event(String name, long number) throws ValidationException {
        setName(name);
        setNumber(number);
    }
    public Event() {}
    public String getName() {
        return name;
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
    public float getNumber() {
        return number;
    }
    public void setNumber(float num) throws ValidationException {
        if (!(num > 0)){
            throw new ValidationException("Количество билетов не может быть меньше нуля.");
        }
        this.number = num;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    /*public void setSales(float number) throws ValidationException {
        if (tracks < 0) {
            throw new ValidationException("Количество продаж не может быть меньше 0.");
        }

        this.number = number;
    }*/
    /*public int getTracks() {
        return tracks;
    }
    public void setTracks(int tracks) throws ValidationException {
        if (tracks < 0) {
            throw new ValidationException("Количество треков в альбоме не может быть меньше 0.");
        }
        this.tracks = tracks;
    }*/
}