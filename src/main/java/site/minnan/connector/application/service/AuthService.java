package site.minnan.connector.application.service;

import site.minnan.connector.domain.aggregrate.AuthUser;
import site.minnan.connector.domain.vo.auth.LoginVO;
import site.minnan.connector.userinterface.dto.auth.AddAuthUserDTO;
import site.minnan.connector.userinterface.dto.auth.PasswordLoginDTO;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * 入口service
 *
 * @author Minnan on 2021/09/14
 */
public interface AuthService {

    /**
     * 登录
     *
     * @param dto
     * @return
     */
    LoginVO login(PasswordLoginDTO dto, HttpServletResponse response);

    /**
     * 通过用户名查询用户
     *
     * @param username
     * @return
     */
    Optional<AuthUser> loadUserByUsername(String username);

    /**
     * 添加用户
     *
     * @param dto
     */
    void addUser(AddAuthUserDTO dto);

}
