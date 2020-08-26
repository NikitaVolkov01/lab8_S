package command;

import exchange.Response;
import runner.Editor;
import runner.ExecutorException;

import java.io.FileNotFoundException;

public interface Command {
    Response execute(Editor editor) throws ExecutorException, FileNotFoundException;
}