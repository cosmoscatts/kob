package com.kob.backend.controller.user;

import com.kob.backend.common.Result;
import com.kob.backend.controller.user.vo.AcCodeVO;
import com.kob.backend.service.AcAppService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/user/account/acwing/acapp")
public class AcAppController {
    @Resource
    private AcAppService acAppService;

    @GetMapping("/apply_code/")
    public Result<?> applyCode() {
        return acAppService.applyCode();
    }

    @GetMapping("/receive_code/")
    public Result<?> receiveCode(AcCodeVO acCodeVO) {
        return acAppService.receiveCode(acCodeVO);
    }
}
