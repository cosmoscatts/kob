package com.kob.backend.controller.user;

import java.util.Objects;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.kob.backend.biz.user.UserBotBiz;
import com.kob.backend.common.DeleteQuery;
import com.kob.backend.common.PageMap;
import com.kob.backend.common.PageQuery;
import com.kob.backend.common.Result;
import com.kob.backend.controller.user.vo.BotReqVO;
import com.kob.backend.controller.user.vo.BotRespVO;
import com.kob.backend.validation.UpdateGroup;

@RestController
@RequestMapping("/api/user/bot")
public class UserBotController {
    @Resource
    private UserBotBiz userBotBiz;

    @GetMapping("/list")
    public Result<PageMap<BotRespVO>> list(PageQuery pageQuery) {
        return Result.success(userBotBiz.getList(pageQuery));
    }

    @PostMapping("/add")
    public Result<?> addBot(@Valid @RequestBody BotReqVO botReqVO) {
        String errorMessage = userBotBiz.add(botReqVO);
        if (!Objects.isNull(errorMessage))
            return Result.error(errorMessage);
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
