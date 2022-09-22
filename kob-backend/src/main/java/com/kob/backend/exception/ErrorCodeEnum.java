package com.kob.backend.exception;

/**
 * 错误码枚举
 */
public enum ErrorCodeEnum {
    UNKNOWN_EXCEPTION(10000, "系统未知异常"), VALID_EXCEPTION(10001, "参数格式校验失败"), SMS_CODE_EXCEPTION(10002, "验证码获取频率太高，稍后再试"),
    NOT_LOGIN_EXCEPTION(10003, "用户未登录"), USER_EXIST_EXCEPTION(15001, "用户存在"), USER_NOT_EXIST_EXCEPTION(15002, "用户不存在"),
    PHONE_EXIST_EXCEPTION(15003, "手机号存在"), LOGIN_PASSWORD_INVALID_EXCEPTION(15003, "账号密码错误");

    private final int code;

    private final String message;

    ErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
