package com.kob.backend.common;

import com.kob.backend.exception.ErrorCodeEnum;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Result<T> {
    private Integer code;
    private T data;
    private String message;

    public static <T> Result<T> set(Integer code, T data, String message) {
        return new Result<T>().setCode(code).setData(data).setMessage(message);
    }

    public static <T> Result<T> success() {
        return set(0, null, null);
    }

    public static <T> Result<T> success(T data) {
        return set(0, data, null);
    }

    public static <T> Result<T> success(String message) {
        return set(0, null, message);
    }

    public static <T> Result<T> success(T data, String message) {
        return set(0, data, message);
    }

    public static <T> Result<T> error() {
        return set(-1, null, null);
    }

    public static <T> Result<T> error(String message) {
        return set(-1, null, message);
    }

    public static <T> Result<T> error(Integer code) {
        return set(code, null, null);
    }

    public static <T> Result<T> error(ErrorCodeEnum errorCodeEnum) {
        return set(errorCodeEnum.getCode(), null, errorCodeEnum.getMessage());
    }

    public static <T> Result<T> error(Integer code, String message) {
        return set(code, null, message);
    }
}
