package com.kob.backend.biz.user;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kob.backend.biz.user.utils.HttpClientUtil;
import com.kob.backend.common.Result;
import com.kob.backend.controller.user.vo.AcCodeVO;
import com.kob.backend.controller.user.vo.AccountRespVO;
import com.kob.backend.dataobject.UserDO;
import com.kob.backend.service.UserService;
import com.kob.backend.utils.JwtUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.*;

@Service
public class AcAppBizImpl implements AcAppBiz {
    private final static String APP_ID = "3626";
    private final static String APP_SECRET = "7505fa4e14694db9b0993e691f462e76";
    private final static String REDIRECT_URI = "https://app3626.acapp.acwing.com.cn/api/user/account/acwing/acapp/receive_code/"; // 回调链接
    private final static String APPLY_ACCESS_TOKEN_URL = "https://www.acwing.com/third_party/api/oauth2/access_token/";
    private final static String GET_USER_INFO_URL = "https://www.acwing.com/third_party/api/meta/identity/getinfo/";
    private final static Random random = new Random();

    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private UserService userService;

    @Override
    public Result<?> applyCode() {
        Map<String, String> map = new HashMap<>();
        map.put("appid", APP_ID);
        try {
            map.put("redirectUri", URLEncoder.encode(REDIRECT_URI, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Result.error();
        }
        map.put("scope", "userinfo");
        StringBuilder state = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            state.append((char) (random.nextInt(10) + '0'));
        }
        map.put("state", state.toString());
        redisTemplate.opsForValue().set(state.toString(), "true");
        redisTemplate.expire(state.toString(), Duration.ofMinutes(10)); // 10 分钟过期
        return Result.success(map);
    }

    @Override
    public Result<?> receiveCode(AcCodeVO acCodeVO) {
        if (acCodeVO.getCode() == null || acCodeVO.getState() == null)
            return Result.error();
        if (Boolean.FALSE.equals(redisTemplate.hasKey(acCodeVO.getState())))
            return Result.error();
        redisTemplate.delete(acCodeVO.getState());

        List<NameValuePair> nameValuePairList = new LinkedList<>();
        nameValuePairList.add(new BasicNameValuePair("appid", APP_ID));
        nameValuePairList.add(new BasicNameValuePair("secret", APP_SECRET));
        nameValuePairList.add(new BasicNameValuePair("code", acCodeVO.getCode()));

        String getString = HttpClientUtil.get(APPLY_ACCESS_TOKEN_URL, nameValuePairList);
        if (getString == null)
            return Result.error();
        JSONObject getResp = JSONObject.parseObject(getString);
        String accessToken = getResp.getString("access_token");
        String openid = getResp.getString("openid");
        if (accessToken == null || openid == null)
            return Result.error();

        // 根据 openid 判断用户是否存在
        List<UserDO> list = userService.list(Wrappers.<UserDO>lambdaQuery().eq(UserDO::getOpenId, openid));
        if (!list.isEmpty()) { // 存在
            UserDO user = list.get(0);
            String jwt = JwtUtil.createJWT(user.getId().toString());
            return Result.success(new AccountRespVO().setToken(jwt));
        }

        nameValuePairList = new LinkedList<>();
        nameValuePairList.add(new BasicNameValuePair("access_token", accessToken));
        nameValuePairList.add(new BasicNameValuePair("openid", openid));
        getString = HttpClientUtil.get(GET_USER_INFO_URL, nameValuePairList);
        if (getString == null)
            return Result.error();
        getResp = JSONObject.parseObject(getString);
        String username = getResp.getString("username"),
                name = getResp.getString("username");
        String photo = getResp.getString("photo");

        if (username == null || photo == null)
            return Result.error();
        for (int i = 0; i < 100; i++) {
            if (userService.list(Wrappers.<UserDO>lambdaQuery()
                    .eq(UserDO::getUsername, username)).isEmpty())
                break;
            username += (char) (random.nextInt(10) + '0');
            if (i == 99) return Result.error();
        }

        UserDO newUser = new UserDO()
                .setId(null)
                .setUsername(username)
                .setName(name)
                .setPassword(null)
                .setAvatar(photo)
                .setRating(1500)
                .setOpenId(openid)
                .setCreateTime(new Date());
        userService.save(newUser);
        String jwt = JwtUtil.createJWT(newUser.getId().toString());
        return Result.success(new AccountRespVO().setToken(jwt));
    }
}
