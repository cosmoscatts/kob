package com.kob.backend.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.backend.dataobject.RecordDO;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.service.RecordService;

@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, RecordDO> implements RecordService {}
