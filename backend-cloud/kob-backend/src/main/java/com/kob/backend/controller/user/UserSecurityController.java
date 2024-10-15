package com.kob.backend.controller.user;

import com.kob.backend.common.Result;
import com.kob.backend.controller.user.vo.UpdatePasswordReqVO;
import com.kob.backend.controller.user.vo.UserSecurityRespVO;
import com.kob.backend.service.UserSecurityService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/api/user/security")
public class UserSecurityController {
    @Resource
    private UserSecurityService userSecurityService;

    @GetMapping("/check")
    public Result<UserSecurityRespVO> checkSecurity() {
        return Result.success(userSecurityService.checkSecurity());
    }

    @PostMapping("/update/password")
    public Result<?> updatePassword(@Valid @RequestBody UpdatePasswordReqVO passwordReqVO) {
        String errorMessage = userSecurityService.updatePassword(passwordReqVO);
        if (Objects.isNull(errorMessage))
            return Result.success("保存成功");
        return Result.error(errorMessage);
    }
}
