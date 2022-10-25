package com.kob.backend.biz.user;

import com.kob.backend.common.Result;
import com.kob.backend.controller.user.vo.AcCodeVO;

public interface AcAppBiz {
    Result<?> applyCode();

    // 接受请求结果
    Result<?> receiveCode(AcCodeVO acCodeVO);
}
