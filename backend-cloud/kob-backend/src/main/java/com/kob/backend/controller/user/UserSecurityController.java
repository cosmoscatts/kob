package com.kob.backend.controller.user;

import java.util.Objects;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.kob.backend.biz.user.UserSecurityBiz;
import com.kob.backend.common.Result;
import com.kob.backend.controller.user.vo.UpdatePasswordReqVO;
import com.kob.backend.controller.user.vo.UserSecurityRespVO;

@RestController
@RequestMapping("/api/user/security")
public class UserSecurityController {
    @Resource
    private UserSecurityBiz userSecurityBiz;

    @GetMapping("/check")
    public Result<UserSecurityRespVO> checkSecurity() {
        return Result.success(userSecurityBiz.checkSecurity());
    }

    @PostMapping("/update/password")
    public Result<?> updatePassword(@Valid @RequestBody UpdatePasswordReqVO passwordReqVO) {
        String errorMessage = userSecurityBiz.updatePassword(passwordReqVO);
        if (Objects.isNull(errorMessage))
            return Result.success("保存成功");
        return Result.error(errorMessage);
    }
}
