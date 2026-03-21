package com.kob.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kob.model.vo.request.AccountReqVO;
import com.kob.model.vo.response.AccountRespVO;
import com.kob.model.vo.request.UserInfoReqVO;
import com.kob.model.vo.response.UserRespVO;
import com.kob.model.entity.User;

import java.util.List;

public interface UserService extends IService<User> {
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

    List<User> selectUserWithRank(String name, int offset, int size);
}
