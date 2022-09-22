package com.kob.backend.common;

import com.kob.backend.exception.ErrorCodeEnum;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Result<T> {
    private Integer code;
    private T data;
    private String msg;

    public static <T> Result<T> set(Integer code, T data, String msg) {
        return new Result<T>().setCode(code).setData(data).setMsg(msg);
    }

    public static <T> Result<T> success() {
        return set(0, null, null);
    }

    public static <T> Result<T> success(T data) {
        return set(0, data, null);
    }

    public static <T> Result<T> success(String msg) {
        return set(0, null, msg);
    }

    public static <T> Result<T> success(T data, String msg) {
        return set(0, data, msg);
    }

    public static <T> Result<T> error() {
        return set(-1, null, null);
    }

    public static <T> Result<T> error(String msg) {
        return set(-1, null, msg);
    }

    public static <T> Result<T> error(Integer code) {
        return set(code, null, null);
    }

    public static <T> Result<T> error(ErrorCodeEnum errorCodeEnum) {
        return set(errorCodeEnum.getCode(), null, errorCodeEnum.getMsg());
    }

    public static <T> Result<T> error(Integer code, String msg) {
        return set(code, null, msg);
    }
}
