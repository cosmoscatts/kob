package com.kob.backend.biz.user;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kob.backend.controller.user.vo.AccountReqVO;
import com.kob.backend.controller.user.vo.AccountRespVO;
import com.kob.backend.controller.user.vo.UserInfoReqVO;
import com.kob.backend.controller.user.vo.UserRespVO;
import com.kob.backend.convert.UserConverter;
import com.kob.backend.dataobject.UserDO;
import com.kob.backend.exception.BusinessException;
import com.kob.backend.exception.ErrorCodeEnum;
import com.kob.backend.security.UserDetailsImpl;
import com.kob.backend.service.UserService;
import com.kob.backend.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class UserBizImpl implements UserBiz {
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private UserService userService;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public AccountRespVO getToken(AccountReqVO accountReqVO) throws BusinessException {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(accountReqVO.getUsername(), accountReqVO.getPassword());

        Authentication authenticate;
        // 登录失败，抛出自定义异常
        try {
            authenticate = authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new BusinessException(ErrorCodeEnum.LOGIN_PASSWORD_INVALID_EXCEPTION);
        }
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
        UserDO user = loginUser.getUser();
        String jwt = JwtUtil.createJWT(user.getId().toString());

        return new AccountRespVO().setToken(jwt);
    }

    @Override
    public String register(AccountReqVO accountReqVO) {
        String username = accountReqVO.getUsername(), password = accountReqVO.getPassword(),
                reenteredPassword = accountReqVO.getReenteredPassword();
        if (!password.equals(reenteredPassword)) {
            return "两次输入的密码不一致";
        }
        List<UserDO> list = userService.list(Wrappers.<UserDO>lambdaQuery().eq(UserDO::getUsername, username));
        if (!list.isEmpty()) {
            return "用户已存在";
        }
        String encodedPassword = passwordEncoder.encode(password);
        UserDO user = new UserDO().setUsername(username).setName("匿名用户").setPassword(encodedPassword)
                .setRating(1500).setCreateTime(new Date());
        userService.save(user);

        return null;
    }

    @Override
    public UserRespVO getUserInfo() {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        UserDO user = loginUser.getUser();
        return UserConverter.INSTANCE.do2vo(user);
    }

    @Override
    public void updateUserInfo(UserInfoReqVO userInfoReqVO) {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        UserDO user = loginUser.getUser();

        userInfoReqVO.setId(user.getId());
        userService.updateById(UserConverter.INSTANCE.vo2do(userInfoReqVO));
    }
}
