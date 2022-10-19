package com.kob.backend.controller.user.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserSecurityRespVO {
    private Boolean hasPassword;
    private Boolean hasPhone;
    private Boolean hasGithub;
    private Boolean hasBilibili;
    private Boolean hasQQ;
    private Boolean hasWechat;
}
