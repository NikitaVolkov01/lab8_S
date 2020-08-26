package command.commands;

import command.Command;
import exchange.Response;
import runner.Editor;

import java.util.ArrayList;
import java.util.List;

public final class HelpCommand implements Command {
    @Override
    public Response execute(Editor editor) {
        List<String> response = new ArrayList<>();
        response.add(">>>ДОСТУПНЫЕ КОМАНДЫ<<<");
        response.add("info: вывести в стандартный поток вывода информацию о коллекции");
        response.add("show: вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        response.add("add {element}: добавить новый элемент в коллекцию");
        response.add("update id {element}: обновить значение элемента коллекции, id которого равен заданному");
        response.add("remove_by_id {id}: удалить элемент из коллекции по его id");
        response.add("clear: очистить коллекцию");
        response.add("execute_script {file_name}: считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.");
        response.add("exit: завершить программу без сохранения в файл");
        response.add("history: вывести последние 11 команд (без их аргументов)");
        return new Response(true, response);
    }
}