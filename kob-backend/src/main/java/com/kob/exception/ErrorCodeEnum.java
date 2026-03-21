package com.kob.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误码枚举
 * 10xxx - 系统级错误
 * 15xxx - 业务级错误
 */
@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {
    // 系统级
    UNKNOWN_EXCEPTION(10000, "系统未知异常"),
    VALID_EXCEPTION(10001, "参数格式校验失败"),
    SMS_CODE_EXCEPTION(10002, "验证码获取频率太高，稍后再试"),
    NOT_LOGIN_EXCEPTION(10003, "用户未登录"),
    RATE_LIMIT_EXCEPTION(10004, "请求过于频繁"),
    FORBIDDEN_EXCEPTION(10005, "无权访问"),

    // 业务级 - 用户
    USER_EXIST_EXCEPTION(15001, "用户已存在"),
    USER_NOT_EXIST_EXCEPTION(15002, "用户不存在"),
    PHONE_EXIST_EXCEPTION(15003, "手机号已存在"),
    LOGIN_PASSWORD_INVALID_EXCEPTION(15004, "账号或密码错误"),
    ;

    private final int code;
    private final String msg;
}
