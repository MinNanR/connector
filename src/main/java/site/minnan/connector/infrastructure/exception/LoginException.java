package site.minnan.connector.infrastructure.exception;

public class LoginException extends EntityNotExistException{

    public LoginException() {
        super();
    }

    public LoginException(String message) {
        super(message);
    }
}
