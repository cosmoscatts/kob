package com.kob.backend.biz.user;

import javax.annotation.Resource;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.kob.backend.controller.user.vo.AccountReqVO;
import com.kob.backend.controller.user.vo.AccountRespVO;
import com.kob.backend.controller.user.vo.UserRespVO;
import com.kob.backend.dataobject.UserDO;
import com.kob.backend.security.UserDetailsImpl;
import com.kob.backend.utils.JwtUtil;

@Service
public class UserBizImpl implements UserBiz {
    @Resource
    private AuthenticationManager authenticationManager;

    @Override
    public AccountRespVO getToken(AccountReqVO accountReqVO) {
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(accountReqVO.getUsername(), accountReqVO.getPassword());
        // 登录失败，会自动处理
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        UserDetailsImpl loginUser = (UserDetailsImpl)authenticate.getPrincipal();
        UserDO user = loginUser.getUser();
        String jwt = JwtUtil.createJWT(user.getId().toString());

        return new AccountRespVO().setToken(jwt);
    }

    @Override
    public String register(AccountReqVO accountReqVO) {
        return null;
    }

    @Override
    public UserRespVO getUserInfo() {
        return null;
    }
}
