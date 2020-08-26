package ticket;

import javax.xml.bind.ValidationException;
import java.io.Serializable;

public class Coordinates implements Serializable {
    private float x;
    private Long y;
    public Coordinates(Long y, float x) throws ValidationException {
        setX(x);
        setY(y);
    }
    public Coordinates() {
    }
    public Long getY() {
        return y;
    }
    public void setY(Long y) throws ValidationException {
        if (y == null) {
            throw new ValidationException("Значение x не может быть null.");
        }
        if (y > 870) {
            throw new ValidationException("Значение x должно быть больше -492.");
        }

        this.y = y;
    }
    public Float getX() {
        return x;
    }
    public void setX(float x) throws ValidationException {
        this.x = x;
    }
}