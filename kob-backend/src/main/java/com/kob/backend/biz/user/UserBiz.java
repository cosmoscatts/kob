package com.kob.backend.biz.user;

import com.kob.backend.controller.user.vo.AccountReqVO;
import com.kob.backend.controller.user.vo.AccountRespVO;
import com.kob.backend.controller.user.vo.UserRespVO;

public interface UserBiz {
    // 获取 token
    AccountRespVO getToken(AccountReqVO accountReqVO);

    // 注册
    String register(AccountReqVO accountReqVO);

    // 获取登录用户信息
    UserRespVO getUserInfo();
}
