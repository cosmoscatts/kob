package com.kob.controller.user;

import com.kob.common.DeleteQuery;
import com.kob.common.PageMap;
import com.kob.common.PageQuery;
import com.kob.common.Result;
import com.kob.model.vo.request.BotReqVO;
import com.kob.model.vo.response.BotRespVO;
import com.kob.service.BotService;
import com.kob.validation.UpdateGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/user/bot")
@RequiredArgsConstructor
public class UserBotController {
    private final BotService botService;

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
