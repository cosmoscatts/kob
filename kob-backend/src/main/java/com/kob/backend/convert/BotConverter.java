package com.kob.backend.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.kob.backend.controller.user.vo.BotReqVO;
import com.kob.backend.controller.user.vo.BotRespVO;
import com.kob.backend.dataobject.BotDO;

@Mapper
public interface BotConverter {
    BotConverter INSTANCE = Mappers.getMapper(BotConverter.class);

    BotRespVO do2vo(BotDO bot);

    BotDO vo2do(BotReqVO botReqVO);
}
