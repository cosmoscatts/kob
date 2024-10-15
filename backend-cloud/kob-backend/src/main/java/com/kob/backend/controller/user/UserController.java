package com.kob.backend.controller.user;

import com.kob.backend.common.Result;
import com.kob.backend.controller.user.vo.AccountReqVO;
import com.kob.backend.controller.user.vo.AccountRespVO;
import com.kob.backend.controller.user.vo.UserInfoReqVO;
import com.kob.backend.service.UserService;
import com.kob.backend.validation.ExtraGroup;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/api/user/account")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 登录获取 token
     */
    @PostMapping("/token")
    public Result<AccountRespVO> getToken(@Valid @RequestBody AccountReqVO accountReqVO) {
        return Result.success(userService.getToken(accountReqVO));
    }

    /**
     * 注册
     */
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
}
