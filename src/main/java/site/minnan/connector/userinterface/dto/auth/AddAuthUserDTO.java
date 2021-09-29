package site.minnan.connector.userinterface.dto.auth;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 添加用户参数
 *
 * @author Minnan on 2021/09/28
 */
@Data
public class AddAuthUserDTO {

    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    private String username;

    /**
     * 密码（MD5加密）
     */
    @NotEmpty(message = "密码不能为空")
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 角色
     */
    private String role;
}
