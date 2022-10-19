package com.kob.backend.biz.user;

import com.kob.backend.controller.user.vo.UpdatePasswordReqVO;
import com.kob.backend.controller.user.vo.UserSecurityRespVO;

public interface UserSecurityBiz {
    // 检查用户的安全设置
    UserSecurityRespVO checkSecurity();

    // 修改用户密码
    String updatePassword(UpdatePasswordReqVO passwordReqVO);
}
