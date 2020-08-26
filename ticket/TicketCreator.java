package ticket;

import io.Input;
import io.Output;
import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

public class TicketCreator {
    Input in;
    Output out;
    ArrayList<Ticket> collection;
    public TicketCreator(Input in, Output out, ArrayList<Ticket> arr) {
        this.in = in;
        this.out = out;
        this.collection = arr;
    }
    private String yesNoInput() {
        String string = in.readLine();
        while (!string.equalsIgnoreCase("y") && !string.equalsIgnoreCase("n")) {
            System.out.print("Неверный формат, пожалуйста, повторите ввод [y/n]: ");
            string = in.readLine();
        }
        return string;
    }
    public boolean inputValue(String message, boolean required, Predicate<String> lambda) {
        while (true) {
            System.out.print(message);
            String input = in.readLine();
            if (input == null) {
                System.out.print(System.lineSeparator() + "Неверный формат. ");
                in = new Input();
            } else if (input.equals("cancel")) {
                System.out.println("Отмена создания нового элемента");
                return false;
            } else if (input.equals("")) {
                if (required) System.out.print("Обязательный параметр. ");
                else return true;
            } else if (lambda.test(input)) {
                return true;
            }
        }
    }
    public Ticket create() {
        System.out.println(
                "Введите необходимые данные или нажмите Enter для пропуска параметра, если это возможно (введите cancel для отмены)");
        Ticket ticket = new Ticket();
        if (!inputValue(
                "Введите имя: ",
                true,
                input -> {
                    try {
                        ticket.setName(input);
                        return true;
                    } catch (ValidationException e) {
                        System.out.print(String.format("%s %s ", "Неверное значение.", e.getMessage()));
                        return false;
                    }
                })) return null;
        Coordinates coordinates = new Coordinates();
        if (!inputValue(
                "Введите x: ",
                true,
                input -> {
                    try {
                        coordinates.setX(Long.parseLong(input));
                        return true;
                    } catch (NumberFormatException e) {
                        System.out.print("Неверный формат. ");
                        return false;
                    } catch (ValidationException e) {
                        System.out.print(String.format("%s %s ", "Неверное значение.", e.getMessage()));
                        return false;
                    }
                })) return null;
        if (!inputValue(
                "Введите y: ",
                true,
                input -> {
                    try {
                        coordinates.setY(Long.parseLong(input));
                        return true;
                    } catch (NumberFormatException e) {
                        System.out.print("Неверный формат. ");
                        return false;
                    } catch (ValidationException e) {
                        System.out.print(String.format("%s %s ", "Неверное значение.", e.getMessage()));
                        return false;
                    }
                })) return null;
        try {
            ticket.setCoordinates(coordinates);
        } catch (ValidationException e) {
            System.out.print("Неверное значение. " + e.getMessage() + " ");
        }
        if (!inputValue(
                "Введите количество участников: ",
                true,
                input -> {
                    try {
                        ticket.setCost(Long.parseLong(input));
                        return true;
                    } catch (NumberFormatException e) {
                        System.out.print("Неверный формат. ");
                        return false;
                    } catch (ValidationException e) {
                        System.out.print(String.format("%s %s ", "Неверное значение.", e.getMessage()));
                        return false;
                    }
                })) return null;
        if (!inputValue(
                "Введите жанр " + Arrays.toString(TicketType.values()) + ": ",
                false,
                input -> {
                    try {
                        ticket.setType(TicketType.valueOf(input));
                        return true;
                    } catch (IllegalArgumentException e) {
                        System.out.print("Неверное значение. ");
                        return false;
                    }
                })) return null;
        System.out.print("Добавить албом?[y/n]: ");
        String includeAlbum = yesNoInput();
        if (includeAlbum.equalsIgnoreCase("y")) {
            Event event = new Event();
            if (!inputValue(
                    "Введите название альбома: ",
                    true,
                    input -> {
                        try {
                            event.setName(input);
                            return true;
                        } catch (ValidationException e) {
                            System.out.print(String.format("%s %s ", "Неверное значение.", e.getMessage()));
                            return false;
                        }
                    })) return null;
            if (!inputValue(
                    "Введите количество треков: ",
                    true,
                    input -> {
                        try {
                            event.setNumber(Integer.parseInt(input));
                            return true;
                        } catch (NumberFormatException e) {
                            System.out.print("Неверный формат. ");
                            return false;
                        } catch (ValidationException e) {
                            System.out.print(String.format("%s %s ", "Неверное значение.", e.getMessage()));
                            return false;
                        }
                    })) return null;

            int newnum;
            Event e = collection.stream().max((a, b) -> (Integer.compare((int)a.getEvent().getId(), (int)b.getEvent().getId()))).get().getEvent();
            if (e == null) newnum = 1;
            else newnum = (int) (e.getId() + 1);

            event.setId(newnum);
            ticket.setevent(event);
        }
        return ticket;
    }
}
