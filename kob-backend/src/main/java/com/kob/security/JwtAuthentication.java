package com.kob.security;

import com.kob.exception.BusinessException;
import com.kob.exception.ErrorCodeEnum;
import com.kob.security.JwtUtil;

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
