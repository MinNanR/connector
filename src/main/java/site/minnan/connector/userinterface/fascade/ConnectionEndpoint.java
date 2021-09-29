package site.minnan.connector.userinterface.fascade;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.minnan.connector.application.service.ConnectionService;
import site.minnan.connector.domain.entity.connection.Command;
import site.minnan.connector.userinterface.dto.WebsocketSocketDTO;
import site.minnan.connector.userinterface.response.WebsocketMessage;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * 执行命令的websocket连接
 *
 * @author Minnan on 2021/09/27
 */
@ServerEndpoint("/connector/command/{id}/{name}")
@Component
@Slf4j
public class ConnectionEndpoint {

    private static ConnectionService connectionService;

    /**
     * 状态码，显示消息
     */
    private static final int STATUS_CODE_MESSAGE = 2;

    /**
     * 状态码，发送数据
     */
    private static final int STATUS_CODE_DATA = 3;

    /**
     * 操作码，执行命令
     */
    private static final int OPERATION_EXECUTE = 1;

    private final Map<Integer, Consumer<WebsocketSocketDTO>> operationMap;

    {
        operationMap = new HashMap<>();
        operationMap.put(OPERATION_EXECUTE, this::execute);
    }

    /**
     * 数据源的id
     */
    private Integer connectionId;

    private RemoteEndpoint.Basic remote;

    @Autowired
    public void setConnectionService(ConnectionService c) {
        connectionService = c;
    }

    /**
     * 连接成功
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) throws IOException {
        log.info("打开websocket连接，session id = {}", session.getId());
        Map<String, String> pathParameters = session.getPathParameters();
        Integer id = Integer.valueOf(pathParameters.get("id"));
        boolean isExist = connectionService.checkConnectionExist(id);
        if (isExist) {
            this.connectionId = id;
            this.remote = session.getBasicRemote();
            sendMessage("connected");
        } else {
            String name = pathParameters.get("name");
            sendMessage(String.format("%s连接不存在", name));
            onClose(session);
        }
    }

    /**
     * 连接关闭
     *
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
        log.info("关闭websocket连接，session id = {}", session.getId());
    }

    /**
     * 接受到消息
     *
     * @param text
     * @return
     */
    @OnMessage
    public void onMsg(String text) {
        log.info("接受到数据：{}", text);
        WebsocketSocketDTO dto = WebsocketSocketDTO.parseParam(text);
        operationMap.get(dto.getStatusCode()).accept(dto);
    }

    /**
     * 执行命令
     *
     * @param dto
     */
    private void execute(WebsocketSocketDTO dto) {
        CompletableFuture.supplyAsync(() -> {
            Object result = connectionService.doCommand(new Command() {
                @Override
                public String getCommand() {
                    return dto.get("sql", String.class);
                }

                @Override
                public Integer getConnectorId() {
                    return connectionId;
                }
            }, this::sendMessage);
            return WebsocketMessage.transport(STATUS_CODE_DATA, result);
        }).thenAccept(this::sendMessage);
        sendMessage("query is prepared");
    }

    /**
     * 发送显示的消息
     *
     * @param message
     */
    public void sendMessage(String message) {
        WebsocketMessage<String> websocketMessage = WebsocketMessage.message(STATUS_CODE_MESSAGE, message);
        sendMessage(websocketMessage);
    }

    /**
     * 发送数据
     *
     * @param message
     */
    public void sendMessage(WebsocketMessage<?> message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            remote.sendText(jsonObject.toJSONString(0));
        } catch (IOException e) {
            log.error("发送消息到客户端失败", e);
        }
    }

}
