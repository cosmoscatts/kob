package com.kob.backend.controller.user;

import java.util.Objects;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.kob.backend.biz.user.UserBotBiz;
import com.kob.backend.common.DeleteQuery;
import com.kob.backend.common.Result;
import com.kob.backend.controller.user.vo.BotReqVO;

@RestController
@RequestMapping("/user/bot")
public class UserBotController {
    @Resource
    private UserBotBiz userBotBiz;

    @GetMapping("/list")
    public Result<?> list() {
        return null;
    }

    @PostMapping("/add")
    public Result<?> addBot(@Valid @RequestBody BotReqVO botReqVO) {
        userBotBiz.add(botReqVO);
        return Result.success("添加成功");
    }

    @PutMapping("/update")
    public Result<?> updateBot() {
        return null;
    }

    @DeleteMapping("/delete")
    public Result<?> deleteBot(@Valid DeleteQuery query) {
        String errorMessage = userBotBiz.deleteBot(query);
        if (!Objects.isNull(errorMessage))
            return Result.error(errorMessage);
        return Result.success("删除成功");
    }
}
