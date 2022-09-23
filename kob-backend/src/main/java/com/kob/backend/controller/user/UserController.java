package com.kob.backend.controller.user;

import java.util.Objects;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.kob.backend.biz.user.UserBiz;
import com.kob.backend.common.Result;
import com.kob.backend.controller.user.vo.AccountReqVO;
import com.kob.backend.controller.user.vo.AccountRespVO;
import com.kob.backend.validation.ExtraGroup;

@RestController
@RequestMapping("/user/account")
public class UserController {
    @Resource
    private UserBiz userBiz;

    /**
     * 登录获取 token
     */
    @PostMapping("/token")
    public Result<AccountRespVO> getToken(@Valid @RequestBody AccountReqVO accountReqVO) {
        return Result.success(userBiz.getToken(accountReqVO));
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result<?> register(@Validated({ExtraGroup.class}) @RequestBody AccountReqVO accountReqVO) {
        String errorMessage = userBiz.register(accountReqVO);
        if (Objects.isNull(errorMessage))
            return Result.success("注册成功");
        return Result.error(errorMessage);
    }

    /**
     * 获取登录用户信息
     */
    @GetMapping("/info")
    public Result<?> getInfo() {
        return Result.success(userBiz.getUserInfo());
    }
}
