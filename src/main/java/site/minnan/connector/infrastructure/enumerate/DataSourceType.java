package site.minnan.connector.infrastructure.enumerate;

import lombok.extern.slf4j.Slf4j;
import site.minnan.connector.domain.aggregrate.DatabaseConnection;
import site.minnan.connector.domain.entity.connection.Connector;
import site.minnan.connector.domain.entity.connection.mysql.MySQLConnector;

import java.sql.SQLException;
import java.util.Optional;

/**
 * 数据源类型
 * @author  on 2021/09/15
 */
@Slf4j
public enum DataSourceType implements DataSourceTypeStrategy {

    MYSQL("mysql"){
        @Override
        public Optional<Connector> createConnection(DatabaseConnection databaseConnection) {
            try {
                MySQLConnector connector = new MySQLConnector(databaseConnection);
                return Optional.of(connector);
            } catch (ClassNotFoundException | SQLException e) {
                log.error("连接数据库异常", e);
                return Optional.empty();
            }
        }
    };

    String name;

    DataSourceType(String name){
        this.name = name;
    }
}
