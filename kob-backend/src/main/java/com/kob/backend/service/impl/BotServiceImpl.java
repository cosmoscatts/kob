package com.kob.backend.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.backend.dataobject.BotDO;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.service.BotService;

@Service
public class BotServiceImpl extends ServiceImpl<BotMapper, BotDO> implements BotService {}
