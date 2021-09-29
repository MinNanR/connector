package site.minnan.connector.userinterface.fascade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.minnan.connector.application.service.AuthService;
import site.minnan.connector.domain.vo.auth.LoginVO;
import site.minnan.connector.infrastructure.annotations.NotAuth;
import site.minnan.connector.userinterface.dto.auth.PasswordLoginDTO;
import site.minnan.connector.userinterface.response.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/***
 * 权限控制器
 * @author Minnan on 2021/09/14
 */
@RestController
@RequestMapping("connector/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 密码登录
     *
     * @param dto
     * @return
     */
    @PostMapping("/login/password")
    @NotAuth
    public ResponseEntity<LoginVO> login(@RequestBody @Valid PasswordLoginDTO dto, HttpServletResponse response) {
        LoginVO vo = authService.login(dto, response);
        return ResponseEntity.success(vo);
    }
}
