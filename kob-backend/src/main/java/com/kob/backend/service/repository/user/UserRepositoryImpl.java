package com.kob.backend.service.repository.user;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.backend.dataobject.UserDO;
import com.kob.backend.mapper.UserMapper;

@Service
public class UserRepositoryImpl extends ServiceImpl<UserMapper, UserDO> implements UserRepository {}
