package com.kob.service;

import com.kob.model.vo.request.UpdatePasswordReqVO;
import com.kob.model.vo.response.UserSecurityRespVO;

public interface UserSecurityService {
    // 检查用户的安全设置
    UserSecurityRespVO checkSecurity();

    // 修改用户密码
    String updatePassword(UpdatePasswordReqVO passwordReqVO);
}
