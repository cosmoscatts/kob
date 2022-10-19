package com.kob.backend.controller.user.vo;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdatePasswordReqVO {
    @NotBlank(message = "原密码不能为空")
    @Length(min = 6, max = 20, message = "原密码为6-20位的字符")
    private String oldPass;
    @NotBlank(message = "新密码不能为空")
    @Length(min = 6, max = 20, message = "新密码为6-20位的字符")
    private String newPass;
    @NotBlank(message = "确认密码不能为空")
    @Length(min = 6, max = 20, message = "确认密码为6-20位的字符")
    private String reenteredNewPass;
}
