package com.kob.backend.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.kob.backend.controller.user.vo.UserRespVO;
import com.kob.backend.dataobject.UserDO;

@Mapper
public interface UserConverter {
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    UserRespVO do2vo(UserDO user);
}
