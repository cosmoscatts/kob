package com.kob.service;

import com.kob.common.Result;
import com.kob.model.vo.request.AcCodeVO;

public interface AcAppService {
    Result<?> applyCode();

    // 接受请求结果
    Result<?> receiveCode(AcCodeVO acCodeVO);
}
