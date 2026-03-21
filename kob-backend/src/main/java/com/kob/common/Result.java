package com.kob.common;

import com.kob.exception.ErrorCodeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Result<T> {
    private Integer code;
    private T data;
    private String msg;

    private static <T> Result<T> of(Integer code, T data, String msg) {
        return new Result<T>().setCode(code).setData(data).setMsg(msg);
    }

    public static <T> Result<T> success() {
        return of(0, null, null);
    }

    public static <T> Result<T> success(T data) {
        return of(0, data, null);
    }

    public static Result<String> successMsg(String msg) {
        return of(0, null, msg);
    }

    public static <T> Result<T> error(String msg) {
        return of(-1, null, msg);
    }

    public static <T> Result<T> error(Integer code, String msg) {
        return of(code, null, msg);
    }

    public static <T> Result<T> error() {
        return of(-1, null, null);
    }

    public static <T> Result<T> error(ErrorCodeEnum errorCode) {
        return of(errorCode.getCode(), null, errorCode.getMsg());
    }

    public static <T> Result<T> success(T data, String msg) {
        return of(0, data, msg);
    }
}
