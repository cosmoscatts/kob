package com.kob.service.impl;

import com.kob.model.vo.request.UpdatePasswordReqVO;
import com.kob.model.vo.response.UserSecurityRespVO;
import com.kob.model.entity.User;
import com.kob.security.UserDetailsImpl;
import com.kob.service.UserSecurityService;
import com.kob.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSecurityServiceImpl implements UserSecurityService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserSecurityRespVO checkSecurity() {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl)authentication.getPrincipal();
        User user = loginUser.getUser();
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
        User user = loginUser.getUser();

        if (user.getPassword() != null) {
            if (!passwordEncoder.matches(passwordReqVO.getOldPass(), user.getPassword())) {
                return "原密码不正确";
            }
        }

        String encodedPassword = passwordEncoder.encode(passwordReqVO.getNewPass());
        userService.updateById(new User().setId(user.getId()).setPassword(encodedPassword));

        return null;
    }
}
