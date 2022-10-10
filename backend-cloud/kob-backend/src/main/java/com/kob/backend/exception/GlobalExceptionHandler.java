package com.kob.backend.exception;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kob.backend.common.Result;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 业务异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        log.error("BusinessException", e);
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理 GET 请求参数验证抛出的异常
     *
     * @param e BindException 实例
     * @return 错误响应结果
     */
    @ExceptionHandler(BindException.class)
    public Result<?> bindExceptionException(final BindException e) {
        String errorMessage = ErrorCodeEnum.VALID_EXCEPTION.getMsg();
        if (e.getFieldError() != null) {
            errorMessage = e.getFieldError().getDefaultMessage();
        }
        log.error("BindExceptionException", e);
        return Result.error(ErrorCodeEnum.VALID_EXCEPTION.getCode(), errorMessage);
    }

    /**
     * 处理 POST / PUT 请求参数验证抛出的异常
     *
     * @param e MethodArgumentNotValidException 实例
     * @return 错误响应结果
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> methodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error("数据校验失败：{}，异常类型：{}", e.getMessage(), e.getClass());

        BindingResult bindingResult = e.getBindingResult();
        String errorMessage = ErrorCodeEnum.VALID_EXCEPTION.getMsg();

        if (bindingResult.hasErrors()) {
            errorMessage = bindingResult.getFieldErrors().get(0).getDefaultMessage();
        }

        return Result.error(ErrorCodeEnum.VALID_EXCEPTION.getCode(), errorMessage);
    }

    /**
     * 运行时异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntimeException(RuntimeException e) {
        log.error("RuntimeException", e);
        return Result.error(ErrorCodeEnum.UNKNOWN_EXCEPTION.getCode(), ErrorCodeEnum.UNKNOWN_EXCEPTION.getMsg());
    }
}
