package site.minnan.connector.domain.aggregrate;


import lombok.*;

import javax.persistence.*;

/**
 * 项目实体类，是连接对象的一个聚合
 *
 * @author Minnan on 2021/09/27
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 所属用户id
     */
    @Column(name = "user_id")
    public Integer userId;

    /**
     * 工程名称
     */
    @Column(name = "name")
    public String name;
}
