package com.kob.controller.user;

import com.kob.common.Result;
import com.kob.model.vo.request.AcCodeVO;
import com.kob.service.AcAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/account/acwing/acapp")
@RequiredArgsConstructor
public class AcAppController {
    private final AcAppService acAppService;

    @GetMapping("/apply_code/")
    public Result<?> applyCode() {
        return acAppService.applyCode();
    }

    @GetMapping("/receive_code/")
    public Result<?> receiveCode(AcCodeVO acCodeVO) {
        return acAppService.receiveCode(acCodeVO);
    }
}
