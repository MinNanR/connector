package site.minnan.connector.domain.entity.connection.mysql;

import cn.hutool.core.util.ReUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import site.minnan.connector.domain.aggregrate.DatabaseConnection;
import site.minnan.connector.domain.entity.connection.Connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.BiFunction;

@Slf4j
public class MySQLConnector implements Connector {

    private Connection conn;

    private final DatabaseConnection info;

    private static final Map<String, BiFunction<Connection, String, Object>> executorMap;

    static {
        executorMap = new HashMap<>();
        executorMap.put("select", MySQLConnector::executeQuery);
    }
//
//    /**
//     * 上次使用时间
//     */
//    private Date lastUsed;

    public MySQLConnector(DatabaseConnection connection) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = connection.getUrl();
        this.info = connection;
        this.conn = DriverManager.getConnection(url, connection.getUsername(), connection.getPassword());
    }

    /**
     * 获得连接对象
     *
     * @return
     */
    @Override
    public Connection getConnector() {
        return conn;
    }

    /**
     * 关闭连接
     */
    @Override
    public void closeConnector() {
        try {
            conn.close();
        } catch (SQLException throwables) {
            log.error("关闭数据库连接异常", throwables);
        }
    }

    /**
     * 确认连接是否打开，否则连接数据库
     */
    @Override
    public void confirmConnected() {
        try {
            if (conn.isClosed()) {
                this.conn = DriverManager.getConnection(info.getUrl(), info.getUsername(), info.getPassword());
            }
        } catch (SQLException throwables) {
            log.error("数据库连接异常", throwables);
        }
    }

    /**
     * 执行命令
     *
     * @param command
     * @return
     */
    @Override
    public Object execute(String command) {
        String sqlType = command.trim().split(" ")[0].toLowerCase();
        if (executorMap.containsKey(sqlType)) {
            return executorMap.get(sqlType).apply(this.conn, command);
        } else {
            log.warn("unsupported operation {}", sqlType);
            throw new UnsupportedOperationException("unsupported operation " + sqlType);
        }
    }

    /**
     * 执行查询命令
     *
     * @param conn 连接对象
     * @param sql  执行的sql
     * @return 查询结果
     */
    private static Object executeQuery(Connection conn, String sql) {
        try {
            ResultSet resultSet = conn.createStatement().executeQuery(sql);
            String columnString = ReUtil.get("\\b(select|SELECT)\\b (.*?) \\b(from|FROM)\\b .*", sql, 2);
            String[] columns = columnString.split(",");
            List<String> columnList = new ArrayList<>();
            for (String column : columns) {
                String[] columnAndColumnName = column.trim().split("\\s");
                columnList.add(columnAndColumnName.length == 1 ? columnAndColumnName[0] :
                        columnAndColumnName[columnAndColumnName.length - 1]);
            }
            JSONArray data = new JSONArray();
            int columnCount = columnList.size();
            while (resultSet.next()) {
                JSONObject row = new JSONObject(true);
                for (int i = 0; i < columnCount; i++) {
                    String columnContent = resultSet.getString(i + 1);
                    row.putOpt(columnList.get(i), columnContent);
                }
                data.add(row);
            }
            return new QueryResult(columnList, data);
        } catch (SQLException e) {
            log.error("执行查询语句异常", e);
            return null;
        }
    }

//    /**
//     * 长时间未使用则关闭数据库连接
//     */
//    @Override
//    public void autoClose(Date date) {
//        if (lastUsed.getTime() < date.getTime()) {
//            closeConnector();
//        }
//    }
}
