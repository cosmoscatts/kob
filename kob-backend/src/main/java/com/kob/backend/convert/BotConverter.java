package com.kob.backend.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BotConverter {
    BotConverter INSTANCE = Mappers.getMapper(BotConverter.class);
}
