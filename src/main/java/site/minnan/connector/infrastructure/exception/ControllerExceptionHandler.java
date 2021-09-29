package site.minnan.connector.infrastructure.exception;


import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import site.minnan.connector.domain.vo.LackParamMessage;
import site.minnan.connector.userinterface.response.ResponseCode;
import site.minnan.connector.userinterface.response.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局统一异常处理器（仅处理在controller内的异常）
 *
 * @author Minnan on 2020/12/17
 */
@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    /**
     * 参数非法或缺失时的异常
     *
     * @param ex 异常
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<List<LackParamMessage>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error(StrUtil.format("Parameter Error,execute in : {},target : {}", ex.getParameter().getExecutable(),
                ex.getBindingResult().getTarget()), ex);
        List<LackParamMessage> details = ex.getBindingResult().getAllErrors().stream()
                .map(error -> (FieldError) error)
                .map(error -> new LackParamMessage(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return ResponseEntity.fail(ResponseCode.INVALID_PARAM, details);
    }

    /**
     * 实体已存在异常（唯一约束不通过）
     *
     * @param ex 异常
     * @return
     */
    @ExceptionHandler(EntityAlreadyExistException.class)
    @ResponseBody
    public ResponseEntity<?> handleEntityAlreadyExistException(EntityAlreadyExistException ex,
                                                               HandlerMethod method) {
        log.error("", ex);
        return ResponseEntity.fail(ex.getMessage());
    }

    /**
     * 处理登录异常
     *
     * @param ex
     * @return
     */
    public ResponseEntity<?> handleLoginException(LoginException ex) {
        log.warn("",ex);
        return ResponseEntity.fail(ex.getMessage());
    }

    /**
     * 处理实体不存在异常，通常发生在查询详情或更新实体时
     *
     * @param ex 异常
     * @return
     */
    @ExceptionHandler(EntityNotExistException.class)
    @ResponseBody
    public ResponseEntity<?> handleEntityNotExistException(EntityNotExistException ex, HandlerMethod method) {
        log.error("", ex);
        return ResponseEntity.fail(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("参数错误", ex);
        return ResponseEntity.fail(ResponseCode.INVALID_PARAM, "参数填写错误，请检查");
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<?> handleUnknownException(Exception ex) {
        log.error("unknown error", ex);
        return ResponseEntity.fail(ResponseCode.UNKNOWN_ERROR);
    }
}
