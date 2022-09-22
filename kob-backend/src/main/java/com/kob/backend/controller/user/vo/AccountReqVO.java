package com.kob.backend.controller.user.vo;

import javax.validation.constraints.NotBlank;

import com.kob.backend.validation.ExtraGroup;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AccountReqVO {
    @NotBlank(message = "账号不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "确认密码不能为空", groups = {ExtraGroup.class})
    private String reenteredPassword;
}
