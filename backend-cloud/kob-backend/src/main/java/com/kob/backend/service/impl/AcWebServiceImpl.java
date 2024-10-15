package com.kob.backend.service.impl;

import com.kob.backend.common.Result;
import com.kob.backend.controller.user.vo.AcCodeVO;
import com.kob.backend.service.AcWebService;
import org.springframework.stereotype.Service;

@Service
public class AcWebServiceImpl implements AcWebService {
    @Override
    public Result<?> applyCode() {
        return null;
    }

    @Override
    public Result<?> receiveCode(AcCodeVO acCodeVO) {
        return null;
    }
}
