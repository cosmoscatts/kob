package com.kob.backend.biz.user;

import com.kob.backend.controller.user.vo.AccountReqVO;
import com.kob.backend.controller.user.vo.AccountRespVO;

public interface UserBiz {
    // 获取 token
    AccountRespVO getToken(AccountReqVO accountReqVO);
}
