package site.minnan.connector.userinterface.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * websocket消息传送实体类
 *
 * @author Minnan on 2021/09/27
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebsocketMessage<T> {

    /**
     * 状态码，每个接口不同
     * 特殊不可自定义的状态码：
     * 0: 连接成功的消息
     * -1: 打开连接失败的消息
     * -2: 关闭连接的消息
     */
    Integer statusCode;

    /**
     * 消息
     */
    String message;

    /**
     * 传输的数据
     */
    T data;

    private WebsocketMessage(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public static WebsocketMessage<?> connected(String message) {
        return new WebsocketMessage<>(0, message);
    }

    public static WebsocketMessage<?> connectFailed(String message) {
        return new WebsocketMessage<>(-1, message);
    }

    public static WebsocketMessage<?> connectClose(String message) {
        return new WebsocketMessage<>(-2, message);
    }

    /**
     * 传输数据
     *
     * @param statusCode 状态码
     * @param message    响应消息
     * @param data       传输的数据
     * @param <T>
     * @return
     */
    public static <T> WebsocketMessage<T> transport(Integer statusCode, String message, T data) {
        WebsocketMessage<T> websocketMessage = new WebsocketMessage<>(statusCode, message);
        websocketMessage.data = data;
        return websocketMessage;
    }

    /**
     * 传输数据
     *
     * @param statusCode 状态码
     * @param data       传输的数据
     * @param <T>
     * @return
     */
    public static <T> WebsocketMessage<T> transport(Integer statusCode, T data) {
        return transport(statusCode, "", data);
    }

    /**
     * 传输消息
     *
     * @param statusCode
     * @param message
     * @return
     */
    public static WebsocketMessage<String> message(Integer statusCode, String message) {
        return new WebsocketMessage<>(statusCode, message);
    }
}
