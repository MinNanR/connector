package site.minnan.connector.infrastructure.filter;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import site.minnan.connector.application.service.AuthService;
import site.minnan.connector.domain.aggregrate.AuthUser;
import site.minnan.connector.infrastructure.annotations.NotAuth;
import site.minnan.connector.infrastructure.context.PrincipalContext;
import site.minnan.connector.infrastructure.utils.JwtUtil;
import site.minnan.connector.userinterface.response.ResponseCode;
import site.minnan.connector.userinterface.response.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Component
@Aspect
@Slf4j
public class PrincipalFilter {

    @Value("${jwt.header}")
    private String authenticationHeader;

    private static final List<String> authUrl = ListUtil.toList("/connector/auth/login");
    ////    private static final List<String> authUrl = Collections.emptyList();
//
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthService authService;

    @Autowired
    private HttpServletRequest request;
//
//    /**
//     * The <code>doFilter</code> method of the Filter is called by the container
//     * each time a request/response pair is passed through the chain due to a
//     * client request for a resource at the end of the chain. The FilterChain
//     * passed in to this method allows the Filter to pass on the request and
//     * response to the next entity in the chain.
//     * <p>
//     * A typical implementation of this method would follow the following
//     * pattern:- <br>
//     * 1. Examine the request<br>
//     * 2. Optionally wrap the request object with a custom implementation to
//     * filter content or headers for input filtering <br>
//     * 3. Optionally wrap the response object with a custom implementation to
//     * filter content or headers for output filtering <br>
//     * 4. a) <strong>Either</strong> invoke the next entity in the chain using
//     * the FilterChain object (<code>chain.doFilter()</code>), <br>
//     * 4. b) <strong>or</strong> not pass on the request/response pair to the
//     * next entity in the filter chain to block the request processing<br>
//     * 5. Directly set headers on the response after invocation of the next
//     * entity in the filter chain.
//     *
//     * @param request  The request to process
//     * @param response The response associated with the request
//     * @param chain    Provides access to the next filter in the chain for this
//     *                 filter to pass the request and response to for further
//     *                 processing
//     * @throws IOException      if an I/O error occurs during this filter's
//     *                          processing of the request
//     * @throws ServletException if the processing fails for any other reason
//     */
//    @Override
//
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
//            ServletException {
//        HttpServletRequest req = (HttpServletRequest) request;
//        String requestURI = req.getRequestURI();
//        log.info(requestURI);
//        for (String url : authUrl) {
//            if (requestURI.startsWith(url)) {
//                chain.doFilter(request, response);
//                return;
//            }
//        }
//        String token = req.getHeader(authenticationHeader);
//        if (StrUtil.isBlank(token)) {
//            ResponseEntity<?> responseEntity = ResponseEntity.fail(ResponseCode.INVALID_USER);
//            response.setContentType("application/json;charset=UTF-8");
//            response.getOutputStream().write(JSONUtil.toJsonStr(responseEntity).getBytes());
//            return;
//        }
//        String username = jwtUtil.getUsernameFromToken(token);
//        Optional<AuthUser> userOptional = authService.loadUserByUsername(username);
//        if (userOptional.isPresent() && jwtUtil.validateToken(token, userOptional.get())) {
//            PrincipalContext.setUser(userOptional.get());
//            chain.doFilter(request, response);
//        } else {
//            ResponseEntity<?> responseEntity = ResponseEntity.fail(ResponseCode.INVALID_USER);
//            response.setContentType("application/json;charset=UTF-8");
//            response.getOutputStream().write(JSONUtil.toJsonStr(responseEntity).getBytes());
//        }
//    }

    @Pointcut("execution(public * site.minnan.connector.userinterface.fascade.*Controller..*(..))")
    private void principal() {
    }

    @Around("principal()")
    public Object principalAroundController(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //获取是否需要登录获取的资源
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        NotAuth notAuth = methodSignature.getMethod().getAnnotation(NotAuth.class);
        if (notAuth != null) {
            return proceedingJoinPoint.proceed();
        }
        String token = request.getHeader(authenticationHeader);
        if (StrUtil.isBlank(token)) {
            return ResponseEntity.fail(ResponseCode.INVALID_USER);
        }
        token = token.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        Optional<AuthUser> userOptional = authService.loadUserByUsername(username);
        if (userOptional.isPresent() && jwtUtil.validateToken(token, userOptional.get())) {
            PrincipalContext.setUser(userOptional.get());
            Object result = proceedingJoinPoint.proceed();
            PrincipalContext.removeUser();
            return result;
        } else {
            return ResponseEntity.fail(ResponseCode.INVALID_USER);
        }
    }
}
