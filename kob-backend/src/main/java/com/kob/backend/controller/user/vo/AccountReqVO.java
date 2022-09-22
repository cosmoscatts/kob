package com.kob.backend.controller.user.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AccountReqVO {
    private String username;
    private String password;
    private String reenteredPassword;
}
