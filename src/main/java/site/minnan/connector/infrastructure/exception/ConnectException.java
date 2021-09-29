package site.minnan.connector.infrastructure.exception;

/**
 * 数据库连接异常
 *
 * @author Minnan on 2021/09/15
 */
public class ConnectException extends RuntimeException {

    public ConnectException() {
        super();
    }

    public ConnectException(String message) {
        super(message);
    }
}
