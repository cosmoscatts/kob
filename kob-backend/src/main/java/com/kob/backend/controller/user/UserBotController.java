package com.kob.backend.controller.user;

import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.kob.backend.biz.user.UserBotBiz;
import com.kob.backend.common.DeleteQuery;
import com.kob.backend.common.Result;
import com.kob.backend.controller.user.vo.BotReqVO;
import com.kob.backend.controller.user.vo.BotRespVO;
import com.kob.backend.validation.UpdateGroup;

@RestController
@RequestMapping("/user/bot")
public class UserBotController {
    @Resource
    private UserBotBiz userBotBiz;

    @GetMapping("/list")
    public Result<List<BotRespVO>> list() {
        return Result.success(userBotBiz.getList());
    }

    @PostMapping("/add")
    public Result<?> addBot(@Valid @RequestBody BotReqVO botReqVO) {
        userBotBiz.add(botReqVO);
        return Result.success("添加成功");
    }

    @PutMapping("/update")
    public Result<?> updateBot(@Validated({UpdateGroup.class}) @RequestBody BotReqVO botReqVO) {
        String errorMessage = userBotBiz.update(botReqVO);
        if (!Objects.isNull(errorMessage))
            return Result.error(errorMessage);
        return Result.success("修改成功");
    }

    @DeleteMapping("/delete")
    public Result<?> deleteBot(@Valid DeleteQuery query) {
        String errorMessage = userBotBiz.delete(query);
        if (!Objects.isNull(errorMessage))
            return Result.error(errorMessage);
        return Result.success("删除成功");
    }
}
