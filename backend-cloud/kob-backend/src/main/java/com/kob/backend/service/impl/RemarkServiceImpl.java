package com.kob.backend.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.backend.dataobject.RemarkDO;
import com.kob.backend.mapper.RemarkMapper;
import com.kob.backend.service.RemarkService;

@Service
public class RemarkServiceImpl extends ServiceImpl<RemarkMapper, RemarkDO> implements RemarkService {}
