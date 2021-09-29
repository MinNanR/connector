package site.minnan.connector.application.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.minnan.connector.application.service.ConnectionService;
import site.minnan.connector.domain.aggregrate.AuthUser;
import site.minnan.connector.domain.aggregrate.DatabaseConnection;
import site.minnan.connector.domain.entity.connection.Command;
import site.minnan.connector.domain.entity.connection.Connector;
import site.minnan.connector.domain.repository.DatabaseConnectionRepository;
import site.minnan.connector.domain.vo.ListQueryVO;
import site.minnan.connector.domain.vo.connection.ConnectionVO;
import site.minnan.connector.infrastructure.context.DataSourceContext;
import site.minnan.connector.infrastructure.context.PrincipalContext;
import site.minnan.connector.infrastructure.enumerate.DataSourceType;
import site.minnan.connector.userinterface.dto.connection.AddConnectionDTO;
import site.minnan.connector.userinterface.dto.connection.GetConnectionListDTO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ConnectionServiceImpl implements ConnectionService {

    @Autowired
    private DatabaseConnectionRepository connectionRepository;

    /**
     * 添加连接
     *
     * @param dto
     */
    @Override
    @Transactional
    public void addConnection(AddConnectionDTO dto) {
        DatabaseConnection connection = DatabaseConnection.builder()
                .host(dto.getHost())
                .port(dto.getPort())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .name(dto.getName())
                .url(dto.getUrl())
                .dataSourceType(DataSourceType.valueOf(dto.getDataSourceType()))
                .build();
        connectionRepository.save(connection);
    }

    /**
     * 查询连接列表
     *
     * @param dto
     * @return
     */
    @Override
    public List<ConnectionVO> getConnectionList(GetConnectionListDTO dto) {
        List<DatabaseConnection> list = connectionRepository.findAllByProjectId(dto.getProjectId());
        return list.stream().map(e -> new ConnectionVO(e.getId(), e.getName())).collect(Collectors.toList());
    }

    /**
     * 确认连接是否可用
     *
     * @param id
     */
    @Override
    public boolean checkConnectionExist(Integer id) {
        Optional<DatabaseConnection> connection = connectionRepository.findById(id);
        connection.ifPresent(e -> DataSourceContext.connect(e, false));
        return connection.isPresent();
    }

    /**
     * 执行命令
     *
     * @param command
     * @return
     */
    @Override
    public Object doCommand(Command command, Consumer<String> response) {
        Connector connector = DataSourceContext.getConnector(command.getConnectorId());
        String commandString = command.getCommand();
        log.info("{} : query is executing", commandString);
        response.accept("query is executing");
        Object result = connector.execute(commandString);
        log.info("{} : query is done", commandString);
        response.accept("query is done, handling response data");
        return result;
    }
}
