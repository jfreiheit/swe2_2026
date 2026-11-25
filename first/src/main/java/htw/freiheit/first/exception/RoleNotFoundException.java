package htw.freiheit.first.exception;

/**
 * Eine angefragte Rolle (z.B. "user") existiert nicht.
 */
public class RoleNotFoundException extends RuntimeException
{
    public RoleNotFoundException(String message) {
        super(message);
    }
}
