package com.kob.backend.controller.user;

import com.kob.backend.biz.user.AcAppBiz;
import com.kob.backend.common.Result;
import com.kob.backend.controller.user.vo.AcCodeVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/user/account/acwing/acapp")
public class AcAppController {
    @Resource
    private AcAppBiz acAppBiz;

    @GetMapping("/apply_code/")
    public Result<?> applyCode() {
        return acAppBiz.applyCode();
    }

    @GetMapping("/receive_code/")
    public Result<?> receiveCode(AcCodeVO acCodeVO) {
        return acAppBiz.receiveCode(acCodeVO);
    }
}
