package com.kob.common;

import com.kob.exception.BusinessException;
import com.kob.exception.ErrorCodeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 接口限流 AOP 切面
 * 使用 Redis 计数器实现滑动窗口限流
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {

    private final StringRedisTemplate redisTemplate;

    @Around("@annotation(com.kob.common.RateLimit)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);

        String key = buildKey(rateLimit, method);
        long count = incrementAndGet(key, rateLimit.window());

        if (count > rateLimit.maxRequests()) {
            log.warn("接口限流触发 [key={}, count={}, max={}]", key, count, rateLimit.maxRequests());
            throw new BusinessException(ErrorCodeEnum.RATE_LIMIT_EXCEPTION, rateLimit.message());
        }

        return joinPoint.proceed();
    }

    /**
     * 构建限流 key：rate_limit:{dimension}:{method}:{identifier}
     */
    private String buildKey(RateLimit rateLimit, Method method) {
        String methodName = method.getDeclaringClass().getSimpleName() + "." + method.getName();
        String identifier;

        if ("user".equals(rateLimit.dimension())) {
            identifier = getCurrentUserId();
        } else {
            identifier = getClientIp();
        }

        return "rate_limit:" + rateLimit.dimension() + ":" + methodName + ":" + identifier;
    }

    /**
     * Redis 原子递增并设置过期时间
     */
    private long incrementAndGet(String key, int windowSeconds) {
        Long count = redisTemplate.opsForValue().increment(key);
        if (count != null && count == 1) {
            redisTemplate.expire(key, windowSeconds, TimeUnit.SECONDS);
        }
        return count != null ? count : 0;
    }

    private String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "anonymous";
    }

    private String getClientIp() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) return "unknown";

        HttpServletRequest request = attrs.getRequest();
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // X-Forwarded-For 可能包含多个 IP，取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip != null ? ip : "unknown";
    }
}
