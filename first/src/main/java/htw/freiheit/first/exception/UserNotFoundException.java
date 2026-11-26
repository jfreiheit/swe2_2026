package htw.freiheit.first.exception;

public class UserNotFoundException extends RuntimeException
{
    public UserNotFoundException(String message) {
        super(message);
    }
}
