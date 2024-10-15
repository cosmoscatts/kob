package com.kob.backend.service.impl;

import com.kob.backend.controller.user.vo.UpdatePasswordReqVO;
import com.kob.backend.controller.user.vo.UserSecurityRespVO;
import com.kob.backend.dataobject.UserDO;
import com.kob.backend.security.UserDetailsImpl;
import com.kob.backend.service.UserSecurityService;
import com.kob.backend.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserSecurityServiceImpl implements UserSecurityService {
    @Resource
    private UserService userService;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserSecurityRespVO checkSecurity() {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl)authentication.getPrincipal();
        UserDO user = loginUser.getUser();
        return new UserSecurityRespVO().setHasPassword(user.getPassword() != null);
    }

    @Override
    public String updatePassword(UpdatePasswordReqVO passwordReqVO) {
        if (!passwordReqVO.getNewPass().equals(passwordReqVO.getReenteredNewPass())) {
            return "两次输入的密码不一致";
        }
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl)authentication.getPrincipal();
        UserDO user = loginUser.getUser();

        if (user.getPassword() != null) {
            if (!passwordEncoder.matches(passwordReqVO.getOldPass(), user.getPassword())) {
                return "原密码不正确";
            }
        }

        String encodedPassword = passwordEncoder.encode(passwordReqVO.getNewPass());
        userService.updateById(new UserDO().setId(user.getId()).setPassword(encodedPassword));

        return null;
    }
}
