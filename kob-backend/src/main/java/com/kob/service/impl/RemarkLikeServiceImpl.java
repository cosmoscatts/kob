package com.kob.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.model.entity.RemarkLike;
import com.kob.mapper.RemarkLikeMapper;
import com.kob.service.RemarkLikeService;

@Service
public class RemarkLikeServiceImpl extends ServiceImpl<RemarkLikeMapper, RemarkLike> implements RemarkLikeService {}
