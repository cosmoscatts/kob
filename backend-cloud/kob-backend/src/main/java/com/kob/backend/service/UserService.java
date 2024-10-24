package com.kob.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kob.backend.controller.user.vo.AccountReqVO;
import com.kob.backend.controller.user.vo.AccountRespVO;
import com.kob.backend.controller.user.vo.UserInfoReqVO;
import com.kob.backend.controller.user.vo.UserRespVO;
import com.kob.backend.dataobject.UserDO;

import java.util.List;

public interface UserService extends IService<UserDO> {
    // 获取 token
    AccountRespVO getToken(AccountReqVO accountReqVO);

    // 注册
    String register(AccountReqVO accountReqVO);

    // 获取登录用户信息
    UserRespVO getUserInfo();

    // 更新用户信息
    void updateUserInfo(UserInfoReqVO userInfoReqVO);

    // 根据 id 获取用户信息
    UserRespVO getUserInfoById(Integer id);

    List<UserDO> selectUserWithRank(String name, int offset, int size);
}
