package Excepciones;

public class SerieInvalido extends RuntimeException {
    public SerieInvalido(String message) {
        super(message);
    }
}
