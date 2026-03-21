package com.kob.controller.user;

import com.kob.common.RateLimit;
import com.kob.common.Result;
import com.kob.model.vo.request.AccountReqVO;
import com.kob.model.vo.response.AccountRespVO;
import com.kob.model.vo.request.UserInfoReqVO;
import com.kob.service.UserService;
import com.kob.validation.ExtraGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/api/user/account")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 登录获取 token
     */
    @RateLimit(window = 60, maxRequests = 10, message = "登录尝试过于频繁")
    @PostMapping("/token")
    public Result<AccountRespVO> getToken(@Valid @RequestBody AccountReqVO accountReqVO) {
        return Result.success(userService.getToken(accountReqVO));
    }

    /**
     * 注册
     */
    @RateLimit(window = 60, maxRequests = 5, message = "注册尝试过于频繁")
    @PostMapping("/register")
    public Result<?> register(@Validated({ExtraGroup.class}) @RequestBody AccountReqVO accountReqVO) {
        String errorMessage = userService.register(accountReqVO);
        if (Objects.isNull(errorMessage))
            return Result.success("注册成功");
        return Result.error(errorMessage);
    }

    /**
     * 获取登录用户信息
     */
    @GetMapping("/info")
    public Result<?> getInfo() {
        return Result.success(userService.getUserInfo());
    }

    @PutMapping("/info")
    public Result<?> updateInfo(@RequestBody UserInfoReqVO userInfoReqVO) {
        userService.updateUserInfo(userInfoReqVO);
        return Result.success("修改成功");
    }

    @GetMapping("/infoById")
    public Result<?> getInfoById(@RequestParam Integer id) {
        return Result.success(userService.getUserInfoById(id));
    }
}
