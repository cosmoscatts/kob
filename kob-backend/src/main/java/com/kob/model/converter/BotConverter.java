package com.kob.model.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.kob.model.vo.request.BotReqVO;
import com.kob.model.vo.response.BotRespVO;
import com.kob.model.entity.Bot;

@Mapper
public interface BotConverter {
    BotConverter INSTANCE = Mappers.getMapper(BotConverter.class);

    BotRespVO do2vo(Bot bot);

    Bot vo2do(BotReqVO botReqVO);
}
