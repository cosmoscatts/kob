package com.kob.backend.consumer.utils;

import com.kob.backend.exception.BusinessException;
import com.kob.backend.exception.ErrorCodeEnum;
import com.kob.backend.utils.JwtUtil;

import io.jsonwebtoken.Claims;

public class JwtAuthentication {
    /**
     * 根据 token 获取用户 id
     */
    public static Integer getUserId(String token) {
        int userId;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            throw new BusinessException(ErrorCodeEnum.NOT_LOGIN_EXCEPTION);
        }

        return userId;
    }

}
