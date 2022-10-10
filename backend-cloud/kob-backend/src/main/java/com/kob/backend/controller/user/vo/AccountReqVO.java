package com.kob.backend.controller.user.vo;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.kob.backend.validation.ExtraGroup;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AccountReqVO {
    @NotBlank(message = "账号不能为空")
    @Length(max = 20, message = "账号不能超过20个字符")
    private String username;
    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 20, message = "密码为6-20位的字符")
    private String password;
    @NotBlank(message = "确认密码不能为空", groups = {ExtraGroup.class})
    @Length(min = 6, max = 20, message = "确认密码为6-20位的字符", groups = {ExtraGroup.class})
    private String reenteredPassword;
}
