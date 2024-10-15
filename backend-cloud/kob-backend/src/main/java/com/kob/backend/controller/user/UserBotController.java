package com.kob.backend.controller.user;

import com.kob.backend.common.DeleteQuery;
import com.kob.backend.common.PageMap;
import com.kob.backend.common.PageQuery;
import com.kob.backend.common.Result;
import com.kob.backend.controller.user.vo.BotReqVO;
import com.kob.backend.controller.user.vo.BotRespVO;
import com.kob.backend.service.BotService;
import com.kob.backend.validation.UpdateGroup;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/user/bot")
public class UserBotController {
    @Resource
    private BotService botService;

    @GetMapping("/list")
    public Result<PageMap<BotRespVO>> list(PageQuery pageQuery) {
        return Result.success(botService.getList(pageQuery));
    }

    @GetMapping("/machines")
    public Result<List<BotRespVO>> getMachineBots() {
        return Result.success(botService.getMachineBots());
    }

    @PostMapping("/add")
    public Result<?> addBot(@Valid @RequestBody BotReqVO botReqVO) {
        String errorMessage = botService.add(botReqVO);
        if (!Objects.isNull(errorMessage))
            return Result.error(errorMessage);
        return Result.success("添加成功");
    }

    @PutMapping("/update")
    public Result<?> updateBot(@Validated({UpdateGroup.class}) @RequestBody BotReqVO botReqVO) {
        String errorMessage = botService.update(botReqVO);
        if (!Objects.isNull(errorMessage))
            return Result.error(errorMessage);
        return Result.success("修改成功");
    }

    @DeleteMapping("/delete")
    public Result<?> deleteBot(@Valid DeleteQuery query) {
        String errorMessage = botService.delete(query);
        if (!Objects.isNull(errorMessage))
            return Result.error(errorMessage);
        return Result.success("删除成功");
    }
}
