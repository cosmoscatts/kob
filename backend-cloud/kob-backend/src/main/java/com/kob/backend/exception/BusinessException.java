package com.kob.backend.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义业务异常类
 */
@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class BusinessException extends RuntimeException {
    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String msg;

    /**
     * 详细描述
     */
    private String description;

    public BusinessException(int code, String msg, String description) {
        this(code, msg);
        this.description = description;
    }

    public BusinessException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
        this.description = "";
    }

    public BusinessException(ErrorCodeEnum errorCodeEnum) {
        this(errorCodeEnum.getCode(), errorCodeEnum.getMsg());
    }

    public BusinessException(ErrorCodeEnum errorCodeEnum, String description) {
        this(errorCodeEnum.getCode(), errorCodeEnum.getMsg(), description);
    }
}