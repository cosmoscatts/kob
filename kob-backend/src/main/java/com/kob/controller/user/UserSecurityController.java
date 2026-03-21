package com.kob.controller.user;

import com.kob.common.Result;
import com.kob.model.vo.request.UpdatePasswordReqVO;
import com.kob.model.vo.response.UserSecurityRespVO;
import com.kob.service.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/api/user/security")
@RequiredArgsConstructor
public class UserSecurityController {
    private final UserSecurityService userSecurityService;

    @GetMapping("/check")
    public Result<UserSecurityRespVO> checkSecurity() {
        return Result.success(userSecurityService.checkSecurity());
    }

    @PostMapping("/update/password")
    public Result<?> updatePassword(@Valid @RequestBody UpdatePasswordReqVO passwordReqVO) {
        String errorMessage = userSecurityService.updatePassword(passwordReqVO);
        if (Objects.isNull(errorMessage))
            return Result.successMsg("保存成功");
        return Result.error(errorMessage);
    }
}
