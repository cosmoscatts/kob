package com.kob.backend.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.backend.dataobject.RemarkLikeDO;
import com.kob.backend.mapper.RemarkLikeMapper;
import com.kob.backend.service.RemarkLikeService;

@Service
public class RemarkLikeServiceImpl extends ServiceImpl<RemarkLikeMapper, RemarkLikeDO> implements RemarkLikeService {}
