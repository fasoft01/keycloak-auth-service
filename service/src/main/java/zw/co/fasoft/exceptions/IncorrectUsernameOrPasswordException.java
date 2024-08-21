package zw.co.fasoft.exceptions;

/**
 * @author Farai Matsika
 * @date 25/Sep/2023
 */
public class IncorrectUsernameOrPasswordException extends RuntimeException {
    public IncorrectUsernameOrPasswordException(String message) {
        super(message);
    }
}
