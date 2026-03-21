package com.kob.exception;

import com.kob.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        log.warn("业务异常 [code={}, msg={}]", e.getCode(), e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException e) {
        String msg = e.getFieldError() != null ? e.getFieldError().getDefaultMessage() : ErrorCodeEnum.VALID_EXCEPTION.getMsg();
        log.warn("参数绑定异常: {}", msg);
        return Result.error(ErrorCodeEnum.VALID_EXCEPTION.getCode(), msg);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String msg = ErrorCodeEnum.VALID_EXCEPTION.getMsg();
        if (bindingResult.hasErrors()) {
            msg = bindingResult.getFieldErrors().get(0).getDefaultMessage();
        }
        log.warn("参数校验失败: {}", msg);
        return Result.error(ErrorCodeEnum.VALID_EXCEPTION.getCode(), msg);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<?> handleMissingParam(MissingServletRequestParameterException e) {
        log.warn("缺少请求参数: {}", e.getParameterName());
        return Result.error(ErrorCodeEnum.VALID_EXCEPTION.getCode(), "缺少参数: " + e.getParameterName());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.warn("请求体解析失败: {}", e.getMessage());
        return Result.error(ErrorCodeEnum.VALID_EXCEPTION.getCode(), "请求体格式错误");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<?> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        log.warn("不支持的请求方法: {}", e.getMethod());
        return Result.error(ErrorCodeEnum.UNKNOWN_EXCEPTION.getCode(), "不支持的请求方法: " + e.getMethod());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result<?> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("访问被拒绝: {}", e.getMessage());
        return Result.error(ErrorCodeEnum.FORBIDDEN_EXCEPTION.getCode(), ErrorCodeEnum.FORBIDDEN_EXCEPTION.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error("未捕获异常", e);
        return Result.error(ErrorCodeEnum.UNKNOWN_EXCEPTION.getCode(), ErrorCodeEnum.UNKNOWN_EXCEPTION.getMsg());
    }
}
