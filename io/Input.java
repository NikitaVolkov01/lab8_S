package io;

import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Input {
    private Scanner reader;
    public Input() {
        reader = new Scanner(new InputStreamReader(System.in));
    }
    public Input(Scanner reader) {
        this.reader = reader;
    }
    public String readLine() {
        try {
            return reader.nextLine().trim().replaceAll(" +", " ");
        } catch (NoSuchElementException | NullPointerException e) {
            return null;
        }
    }
}
