package spring.security.exceptions;

public class UsernameExistsException extends Throwable {
    public UsernameExistsException(final String message) {
        super(message);
    }
}
