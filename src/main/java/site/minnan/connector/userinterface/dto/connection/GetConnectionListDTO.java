package site.minnan.connector.userinterface.dto.connection;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 连接列表查询参数
 *
 * @author Minnan on 2021/09/27
 */
@Data
public class GetConnectionListDTO {

    /**
     * 工程id
     */
    @NotNull(message = "未指定工程")
    private Integer projectId;
}
