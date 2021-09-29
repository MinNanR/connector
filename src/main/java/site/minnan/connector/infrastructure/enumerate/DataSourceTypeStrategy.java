package site.minnan.connector.infrastructure.enumerate;

import site.minnan.connector.domain.aggregrate.DatabaseConnection;
import site.minnan.connector.domain.entity.connection.Connector;

import java.util.Optional;

/**
 * 数据源相关策略
 * @author Minnan on 2021/09/15
 */
public interface DataSourceTypeStrategy {

    Optional<Connector> createConnection(DatabaseConnection databaseConnection);
}
