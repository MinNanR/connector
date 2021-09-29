package site.minnan.connector.application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.minnan.connector.application.service.AuthService;
import site.minnan.connector.domain.aggregrate.AuthUser;
import site.minnan.connector.domain.repository.AuthUserRepository;
import site.minnan.connector.domain.vo.auth.LoginVO;
import site.minnan.connector.infrastructure.enumerate.Role;
import site.minnan.connector.infrastructure.exception.EntityAlreadyExistException;
import site.minnan.connector.infrastructure.exception.EntityNotExistException;
import site.minnan.connector.infrastructure.exception.LoginException;
import site.minnan.connector.infrastructure.utils.JwtUtil;
import site.minnan.connector.infrastructure.utils.PasswordEncoder;
import site.minnan.connector.userinterface.dto.auth.AddAuthUserDTO;
import site.minnan.connector.userinterface.dto.auth.PasswordLoginDTO;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

/**
 * 登录service
 *
 * @author Minnan on 2021/09/14
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${jwt.header}")
    private String authenticationHeader;

    /**
     * 登录
     *
     * @param dto
     * @return
     */
    @Override
    public LoginVO login(PasswordLoginDTO dto, HttpServletResponse response) {
        Optional<AuthUser> userOptional = authUserRepository.getAuthUserByUsername(dto.getUsername());
        AuthUser user = userOptional.orElseThrow(() -> new LoginException("用户不存在"));
        String key = user.getKey();
        PasswordEncoder passwordEncoder = new PasswordEncoder(key);
        if (!passwordEncoder.match(dto.getPassword(), user.getPassword())) {
            throw new LoginException("密码错误");
        }
        String token = "Bearer " + jwtUtil.generateToken(user);
        return LoginVO.builder().authority(token).username(user.getUsername()).build();
    }

    /**
     * 通过用户名查询用户
     *
     * @param username
     * @return
     */
    @Override
    public Optional<AuthUser> loadUserByUsername(String username) {
        return authUserRepository.getAuthUserByUsername(username);
    }

    /**
     * 添加用户
     *
     * @param dto
     */
    @Override
    public void addUser(AddAuthUserDTO dto) {
//        Optional<AuthUser> opt = authUserRepository.getAuthUserByUsername(dto.getUsername());
//        if (opt.isPresent()) {
//            throw new EntityAlreadyExistException("用户名已存在");
//        }
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        AuthUser authUser = AuthUser.builder()
                .username(dto.getUsername())
                .password(encodedPassword)
                .key(passwordEncoder.getKey())
                .passwordStamp(UUID.randomUUID().toString().replaceAll("-", ""))
                .realName(dto.getRealName())
                .role(Role.valueOf(dto.getRole()))
                .build();
        authUserRepository.save(authUser);
    }
}
