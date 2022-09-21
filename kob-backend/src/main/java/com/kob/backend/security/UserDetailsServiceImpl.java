package com.kob.backend.security;

import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kob.backend.dataobject.UserDO;
import com.kob.backend.service.repository.user.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserRepository userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDO user = userService.getOne(Wrappers.<UserDO>lambdaQuery().eq(UserDO::getUsername, username));
        if (Objects.isNull(user))
            throw new UsernameNotFoundException("用户不存在");

        return new UserDetailsImpl(user);
    }
}
