package com.kob.backend.convert;

import com.kob.backend.controller.rank.vo.RankRespVO;
import com.kob.backend.controller.user.vo.UserInfoReqVO;
import com.kob.backend.controller.user.vo.UserRespVO;
import com.kob.backend.dataobject.UserDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserConverter {
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    UserRespVO do2vo(UserDO user);

    UserDO vo2do(UserInfoReqVO userInfoReqVO);

    @Mapping(target = "rankNum", ignore = true)
    RankRespVO do2RankVO(UserDO user);
}
