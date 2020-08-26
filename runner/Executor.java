package runner;

import command.AvailableCommands;
import command.Command;
import command.commands.AddCommand;
import command.commands.UpdateCommand;
import exchange.Request;
import exchange.Response;
import io.Input;
import io.Output;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Executor class contains main methods that run others
 */
public final class Executor {
    private Input in;
    private Output out;
    private Editor editor;
    private AvailableCommands availableCommands;

    public Executor() {
        in = new Input();
        out = new Output();
        editor = new Editor();
        availableCommands = new AvailableCommands();
    }

    public Input getIn() {
        return in;
    }

    public void setIn(Input in) {
        this.in = in;
    }

    public Output getOut() {
        return out;
    }

    public void setOut(Output out) {
        this.out = out;
    }

    /**
     * Returns editor
     *
     * @return Editor
     */
    public Editor getEditor() {
        return editor;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }


    /**
     * Executes command
     *
     * @param request command to be executed
     * @return Response
     */
    public Response executeCommand(Request request) {
        editor.setValue(request.getCommandParameter());
        Command com = availableCommands.getCommand(request.getCommandName());
        //System.out.println("TICKET FOR ADD or UPDATE" + request.getTicket());
        if (request.getCommandName().trim().equals("add")){
            ((AddCommand) com).addRequest(request);
        }
        if (request.getCommandName().trim().equals("update")){
            System.out.println("UPDATING");
            ((UpdateCommand) com).addRequest(request);
        }
        return executeCom(com, request);
    }

    /**
     * Execute command if it is not null
     *
     * @param com command to execute
     * @return Response
     */
    private Response executeCom(Command com, Request request) {
        String commandName = request.getCommandName();
        List<String> response = new ArrayList<>();
        if (com == null) {
            response.add("Неверная команда. Введите help для отображения списка доступных команд.");
            return new Response(false, response);
        }
        Response commandResponse;
        try {
            commandResponse = com.execute(editor);
        } catch (ExecutorException | FileNotFoundException e) {
            return new Response(false, "Ошибка: " + e.getMessage());
        }
        response.addAll(commandResponse.getResponse());
        if (commandResponse.isCorrect()) {
            editor.getCommandHistory().addCommand(commandName);
        }
        return new Response(true, response);
    }

    /**
     * Returns true if file path is correct
     *
     * @param args arguments with file name
     * @return True if file path is correct
     */
    public boolean setArgs(String[] args) {
        if (args.length == 0) {
            System.err.println("Необходим обязательный аргумент: Полное имя файла данных.");
            return false;
        }
        File file = new File(args[0]);
        if (file.isDirectory()) {
            System.out.println("Обнаружен путь к директории, а не к файлу.");
            return false;
        }
        if (!file.exists()) {
            System.out.println("Файл не найден.");
            return false;
        }
        if (!file.canRead()) {
            System.out.println("Ошибка доступа на чтение.");
            return false;
        }
        if (!file.canWrite()) {
            System.out.println("Ошибка доступа на запись.");
            return false;
        }
        editor.setDataFilePath(args[0]);
        return true;
    }
}