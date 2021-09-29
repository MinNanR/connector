package site.minnan.connector.domain.entity.connection;

/**
 * 命令接口
 * @author Minnan on 2021/09/27
 */
public interface Command {

    String getCommand();

    Integer getConnectorId();
}
