package com.kob.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.dataobject.BotDO;

@Mapper
@Repository
public interface BotMapper extends BaseMapper<BotDO> {}
