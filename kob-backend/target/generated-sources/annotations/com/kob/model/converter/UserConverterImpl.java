package com.kob.model.converter;

import com.kob.model.entity.User;
import com.kob.model.vo.request.UserInfoReqVO;
import com.kob.model.vo.response.RankRespVO;
import com.kob.model.vo.response.UserRespVO;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-21T21:21:39+0800",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 1.8.0_442 (Amazon.com Inc.)"
)
public class UserConverterImpl implements UserConverter {

    @Override
    public UserRespVO do2vo(User user) {
        if ( user == null ) {
            return null;
        }

        UserRespVO userRespVO = new UserRespVO();

        userRespVO.setId( user.getId() );
        userRespVO.setUsername( user.getUsername() );
        userRespVO.setName( user.getName() );
        userRespVO.setPhone( user.getPhone() );
        userRespVO.setEmail( user.getEmail() );
        userRespVO.setAvatar( user.getAvatar() );
        userRespVO.setRating( user.getRating() );
        userRespVO.setOpenId( user.getOpenId() );
        userRespVO.setCreateTime( user.getCreateTime() );

        return userRespVO;
    }

    @Override
    public User vo2do(UserInfoReqVO userInfoReqVO) {
        if ( userInfoReqVO == null ) {
            return null;
        }

        User user = new User();

        user.setId( userInfoReqVO.getId() );
        user.setName( userInfoReqVO.getName() );
        user.setPhone( userInfoReqVO.getPhone() );
        user.setEmail( userInfoReqVO.getEmail() );
        user.setAvatar( userInfoReqVO.getAvatar() );

        return user;
    }

    @Override
    public RankRespVO do2RankVO(User user) {
        if ( user == null ) {
            return null;
        }

        RankRespVO rankRespVO = new RankRespVO();

        rankRespVO.setId( user.getId() );
        rankRespVO.setName( user.getName() );
        rankRespVO.setAvatar( user.getAvatar() );
        rankRespVO.setRating( user.getRating() );
        rankRespVO.setCreateTime( user.getCreateTime() );

        return rankRespVO;
    }
}
