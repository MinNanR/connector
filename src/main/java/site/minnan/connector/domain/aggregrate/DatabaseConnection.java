package site.minnan.connector.domain.aggregrate;

import cn.hutool.core.util.StrUtil;
import lombok.*;
import site.minnan.connector.infrastructure.enumerate.DataSourceType;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "database_connection")
public class DatabaseConnection{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * 地址
     */
    @Column(name = "url")
    private String url;

    /**
     * 主机
     */
    @Column(name = "host")
    private String host;

    /**
     * 端口
     */
    @Column(name = "port")
    private Integer port;

    /**
     * 用户名
     */
    @Column(name = "username")
    private String username;

    /**
     * 密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 数据源类型
     */
    @Enumerated
    @Column(name = "data_source_type")
    private DataSourceType dataSourceType;

    /**
     * 项目id
     */
    @Column(name = "project_id")
    private Integer projectId;
}
