package site.minnan.connector.domain.aggregrate;

import cn.hutool.core.text.csv.CsvWriter;
import lombok.*;
import org.springframework.stereotype.Repository;
import site.minnan.connector.infrastructure.enumerate.Role;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "auth_user")
public class AuthUser {

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户名
     */
    @Column(name = "username")
    private String username;

    /**
     * 昵称
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * 密码
     */
    @Column(name = "password")
    private String password;

    @Enumerated
    @Column(name = "role")
    private Role role;

    /**
     * 密码戳
     */
    @Column(name = "password_stamp")
    private String passwordStamp;

    /**
     * 密钥
     */
    @Column(name = "key")
    private String key;

    public void exportToCsv(CsvWriter writer) {
        writer.write(new String[]{String.valueOf(id), username, password, realName, realName, passwordStamp});
    }
}
