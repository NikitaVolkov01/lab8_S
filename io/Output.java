package io;

import java.io.BufferedOutputStream;
import java.io.IOException;

public class Output {
    private BufferedOutputStream writer;
    public Output() {
        writer = new BufferedOutputStream(System.out);
    }
    public Output(BufferedOutputStream writer) {
        this.writer = writer;
    }
    public void println(String message) {
        try {
            byte[] buffer = (message + System.lineSeparator()).getBytes();
            writer.write(buffer, 0, buffer.length);
            writer.flush();
        } catch (IOException ignored) {
        }
    }
    public void println() {
        try {
            byte[] buffer = System.lineSeparator().getBytes();
            writer.write(buffer, 0, buffer.length);
            writer.flush();
        } catch (IOException ignored) {
        }
    }
    public void print(String message) {
        try {
            byte[] buffer = (message).getBytes();
            writer.write(buffer, 0, buffer.length);
            writer.flush();
        } catch (IOException ignored) {
        }
    }
}
