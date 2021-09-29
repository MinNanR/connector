package site.minnan.connector.domain.vo.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 登录返回参数
 * @author Minnan on 2021/09/14
 */
@Setter
@Getter
@Builder
public class LoginVO {

    /**
     * 返回token
     */
    private String authority;

    /**
     * 用户名
     */
    private String username;
}
