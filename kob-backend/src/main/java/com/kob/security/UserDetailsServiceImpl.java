package com.kob.security;

import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kob.model.entity.User;
import com.kob.service.UserService;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
        if (Objects.isNull(user))
            throw new UsernameNotFoundException("用户不存在");

        return new UserDetailsImpl(user);
    }
}
