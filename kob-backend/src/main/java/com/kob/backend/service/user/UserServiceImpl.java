package com.kob.backend.service.user;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kob.backend.service.repository.user.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserRepository userRepository;

}
