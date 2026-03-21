package com.kob.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口限流注解
 * 基于 Redis 滑动窗口实现，支持按 IP 或用户维度限流
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    /** 时间窗口（秒） */
    int window() default 60;

    /** 窗口内最大请求次数 */
    int maxRequests() default 30;

    /** 限流维度：ip 或 user */
    String dimension() default "ip";

    /** 限流提示信息 */
    String message() default "请求过于频繁，请稍后再试";
}
