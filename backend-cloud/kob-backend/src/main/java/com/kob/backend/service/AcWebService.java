package com.kob.backend.service;

import com.kob.backend.common.Result;
import com.kob.backend.controller.user.vo.AcCodeVO;

public interface AcWebService {
    Result<?> applyCode();

    // 接受请求结果
    Result<?> receiveCode(AcCodeVO acCodeVO);
}
