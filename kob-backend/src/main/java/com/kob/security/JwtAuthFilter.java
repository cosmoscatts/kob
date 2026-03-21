package com.kob.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kob.model.entity.User;
import com.kob.exception.BusinessException;
import com.kob.exception.ErrorCodeEnum;
import com.kob.security.UserDetailsImpl;
import com.kob.service.UserService;
import com.kob.security.JwtUtil;

import io.jsonwebtoken.Claims;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response,
        @NotNull FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (!StringUtils.hasText(token) || !token.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        token = token.substring(7);

        String userId;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            throw new BusinessException(ErrorCodeEnum.NOT_LOGIN_EXCEPTION);
        }

        User user = userService.getById(Integer.parseInt(userId));

        if (user == null) {
            throw new BusinessException(ErrorCodeEnum.USER_NOT_EXIST_EXCEPTION);
        }

        UserDetailsImpl loginUser = new UserDetailsImpl(user);
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginUser, null, null);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
