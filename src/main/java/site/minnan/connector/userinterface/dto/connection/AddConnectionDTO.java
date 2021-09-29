package site.minnan.connector.userinterface.dto.connection;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

/**
 * 添加连接参数
 *
 * @author on 2021/09/15
 */
@Getter
@Setter
public class AddConnectionDTO {

    /**
     * 主机
     */
    @NotEmpty
    private String host;

    /**
     * 端口
     */
    @Max(65535)
    @Min(0)
    private Integer port;

    /**
     * 用户名
     */
    @NotEmpty
    private String username;

    /**
     * 密码
     */
    @NotEmpty
    private String password;

    /**
     * 地址
     */
    private String url;

    /**
     * 连接名称
     */
    private String name;

    /**
     * 数据源类型
     */
    private String dataSourceType;
}
