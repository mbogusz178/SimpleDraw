package schumi178.javaprojects.graphics.zad1.exception;

public class BrokenFileException extends Exception {

    public BrokenFileException() {
        super("Plik jest uszkodzony.");
    }
}
