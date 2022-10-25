package com.kob.backend.biz.user;

import com.kob.backend.common.Result;
import com.kob.backend.controller.user.vo.AcCodeVO;
import org.springframework.stereotype.Service;

@Service
public class AcWebBizImpl implements AcWebBiz {
    @Override
    public Result<?> applyCode() {
        return null;
    }

    @Override
    public Result<?> receiveCode(AcCodeVO acCodeVO) {
        return null;
    }
}
