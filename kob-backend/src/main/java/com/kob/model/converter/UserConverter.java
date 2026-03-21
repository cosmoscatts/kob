package com.kob.model.converter;

import com.kob.model.vo.response.RankRespVO;
import com.kob.model.vo.request.UserInfoReqVO;
import com.kob.model.vo.response.UserRespVO;
import com.kob.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserConverter {
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    UserRespVO do2vo(User user);

    User vo2do(UserInfoReqVO userInfoReqVO);

    @Mapping(target = "rankNum", ignore = true)
    RankRespVO do2RankVO(User user);
}
