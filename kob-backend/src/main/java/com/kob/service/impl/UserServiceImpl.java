package com.kob.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.exception.BusinessException;
import com.kob.exception.ErrorCodeEnum;
import com.kob.mapper.UserMapper;
import com.kob.model.converter.UserConverter;
import com.kob.model.entity.User;
import com.kob.model.vo.request.AccountReqVO;
import com.kob.model.vo.request.UserInfoReqVO;
import com.kob.model.vo.response.AccountRespVO;
import com.kob.model.vo.response.UserRespVO;
import com.kob.security.JwtUtil;
import com.kob.security.UserDetailsImpl;
import com.kob.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(@Lazy AuthenticationManager authenticationManager,
                           PasswordEncoder passwordEncoder,
                           UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

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
        User user = loginUser.getUser();
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
        List<User> list = this.list(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
        if (!list.isEmpty()) {
            return "用户已存在";
        }
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User().setUsername(username).setName("匿名用户").setPassword(encodedPassword)
                .setRating(1500).setCreateTime(new Date());
        this.save(user);
        return null;
    }

    @Override
    public UserRespVO getUserInfo() {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();
        return UserConverter.INSTANCE.do2vo(user);
    }

    @Override
    public void updateUserInfo(UserInfoReqVO userInfoReqVO) {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();

        userInfoReqVO.setId(user.getId());
        this.updateById(UserConverter.INSTANCE.vo2do(userInfoReqVO));
    }

    @Override
    public UserRespVO getUserInfoById(Integer id) {
        User user = this.getById(id);
        return UserConverter.INSTANCE.do2vo(user);
    }

    @Override
    public List<User> selectUserWithRank(String name, int offset, int size) {
        return userMapper.selectUserWithRank(name, offset, size);
    }
}
