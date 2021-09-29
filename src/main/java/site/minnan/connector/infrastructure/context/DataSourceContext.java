package site.minnan.connector.infrastructure.context;

import cn.hutool.core.date.DateTime;
import lombok.extern.slf4j.Slf4j;
import site.minnan.connector.domain.aggregrate.DatabaseConnection;
import site.minnan.connector.domain.entity.connection.Connector;
import site.minnan.connector.infrastructure.exception.ConnectException;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 连接池管理器
 *
 * @author Minnan on 2021/09/15
 */
@Slf4j
public final class DataSourceContext {

    private static final ConcurrentHashMap<Integer, Connector> connectionPool;

    private static final ConcurrentHashMap<Integer, Date> lastUsedTime;

    static {
        connectionPool = new ConcurrentHashMap<>();
        lastUsedTime = new ConcurrentHashMap<>();
    }

    /**
     * 创建连接
     *
     * @param connection
     */
    public static void newConnection(DatabaseConnection connection) {
        Optional<Connector> connectorOptional = connection.getDataSourceType().createConnection(connection);
        Connector connector = connectorOptional.orElseThrow(() -> new ConnectException("连接数据库错误"));
        connectionPool.put(connection.getId(), connector);
        log.info("open connection ---> {}", connection.getUrl());
    }

    /**
     * 连接数据库
     *
     * @param connection 连接信息
     * @param refresh    连接池内有连接是否更新连接
     */
    public static void connect(DatabaseConnection connection, boolean refresh) {
        if (!connectionPool.containsKey(connection.getId())) {
            newConnection(connection);
        } else if (refresh) {
            newConnection(connection);
        }
    }

    public static Connector getConnector(int id) {
//        if (!connectionPool.containsKey(id)) {
//            return Optional.empty();
//        }
//        Connector instance = connectionPool.get(id);
//        ConnectorProxy proxy = new ConnectorProxy(instance, id);
//        return Optional.of(proxy);
        Connector instance = connectionPool.get(id);
        return new ConnectorProxy(instance, id);
    }

    static class ConnectorProxy implements Connector {

        /**
         * 示例对象
         */
        private final Connector instance;

        /**
         * 连接id
         */
        private final Integer connectionId;

        public ConnectorProxy(Connector instance, Integer connectionId) {
            this.instance = instance;
            this.connectionId = connectionId;
        }

        /**
         * 获得链接对象
         *
         * @return
         */
        @Override
        public Object getConnector() {
            return instance;
        }

        /**
         * 关闭连接
         */
        @Override
        public void closeConnector() {
            instance.closeConnector();
        }

        /**
         * 确认连接未关闭，否则重新连接
         */
        @Override
        public void confirmConnected() {
            instance.confirmConnected();
        }

        /**
         * 执行命令
         *
         * @param command
         * @return
         */
        @Override
        public Object execute(String command) {
            confirmConnected();
            Object result = instance.execute(command);
            DateTime now = DateTime.now();
            lastUsedTime.put(connectionId, now);
            return result;
        }
    }
}
