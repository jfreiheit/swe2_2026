package htw.freiheit.studi.exception;

/**
 * Die Anfrage widerspricht dem aktuellen Zustand der Daten,
 * z.B. weil ein Nutzername bereits vergeben ist.
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
