package site.minnan.connector.userinterface.fascade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.minnan.connector.application.service.ConnectionService;
import site.minnan.connector.domain.vo.ListQueryVO;
import site.minnan.connector.domain.vo.connection.ConnectionVO;
import site.minnan.connector.userinterface.dto.connection.AddConnectionDTO;
import site.minnan.connector.userinterface.dto.connection.GetConnectionListDTO;
import site.minnan.connector.userinterface.response.ResponseEntity;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("connector/connection")
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

    @PostMapping("/addConnection")
    public ResponseEntity<?> getConnectionList(@RequestBody @Valid AddConnectionDTO dto) {
        connectionService.addConnection(dto);
        return ResponseEntity.success();
    }

    /**
     * 查询连接列表
     *
     * @param dto
     * @return
     */
    @PostMapping("getConnectionList")
    public ResponseEntity<List<ConnectionVO>> getConnectionList(@RequestBody @Valid GetConnectionListDTO dto) {
        List<ConnectionVO> vo = connectionService.getConnectionList(dto);
        return ResponseEntity.success(vo);
    }
}
