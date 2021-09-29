package site.minnan.connector.application.service;

import site.minnan.connector.domain.entity.connection.Command;
import site.minnan.connector.domain.vo.ListQueryVO;
import site.minnan.connector.domain.vo.connection.ConnectionVO;
import site.minnan.connector.userinterface.dto.connection.AddConnectionDTO;
import site.minnan.connector.userinterface.dto.connection.GetConnectionListDTO;
import site.minnan.connector.userinterface.response.WebsocketMessage;

import java.util.List;
import java.util.function.Consumer;

/**
 * 链接管理service
 *
 * @author Minnan on 2021/09/15
 */
public interface ConnectionService {

    /**
     * 添加连接
     *
     * @param dto
     */
    void addConnection(AddConnectionDTO dto);

    /**
     * 查询连接列表
     *
     * @param dto
     * @return
     */
    List<ConnectionVO> getConnectionList(GetConnectionListDTO dto);

    /**
     * 确认连接是否可用
     *
     * @param id
     */
    boolean checkConnectionExist(Integer id);

    /**
     * 执行命令
     *
     * @param command  执行的命令
     * @param response 响应到前端的处理方法
     * @return
     */
    Object doCommand(Command command, Consumer<String> response);
}
