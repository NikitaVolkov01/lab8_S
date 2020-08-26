package exchange;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class Response implements Serializable {
    private boolean correct;
    private List<String> response;
    public Response(boolean correct, List<String> response) {
        this.correct = correct;
        this.response = response;
    }
    public Response(boolean correct) {
        this.correct = correct;
        this.response = new ArrayList<>();
        this.response.add("");
    }
    public Response(boolean correct, String response) {
        this.correct = correct;
        this.response = new ArrayList<>();
        this.response.add(response);
    }
    public List<String> getResponse() {
        return response;
    }
    public String getString() {
        return String.join("\n", response);
    }
    public boolean isCorrect() {
        return correct;
    }
}