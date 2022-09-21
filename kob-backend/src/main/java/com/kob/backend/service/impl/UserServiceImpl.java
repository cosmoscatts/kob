package com.kob.backend.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.backend.dataobject.UserDO;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.service.UserService;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {}
