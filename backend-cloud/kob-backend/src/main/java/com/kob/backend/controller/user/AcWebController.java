package com.kob.backend.controller.user;

import com.kob.backend.common.Result;
import com.kob.backend.controller.user.vo.AcCodeVO;
import com.kob.backend.service.AcWebService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/user/account/acwing/web")
public class AcWebController {
    @Resource
    private AcWebService acWebService;

    @GetMapping("/apply_code/")
    public Result<?> applyCode() {
        return acWebService.applyCode();
    }

    @GetMapping("/receive_code/")
    public Result<?> receiveCode(AcCodeVO acCodeVO) {
        return acWebService.receiveCode(acCodeVO);
    }
}
