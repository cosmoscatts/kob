package com.kob.backend.controller.rank;

import com.kob.backend.common.PageMap;
import com.kob.backend.common.PageQuery;
import com.kob.backend.common.Result;
import com.kob.backend.controller.rank.vo.RankRespVO;
import com.kob.backend.controller.record.vo.RecordSearchVO;
import com.kob.backend.service.RankService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/rank")
public class RankController {
    @Resource
    private RankService rankService;

    @GetMapping("/list")
    public Result<PageMap<RankRespVO>> getList(PageQuery pageQuery, RecordSearchVO searchVO) {
        return Result.success(rankService.getList(pageQuery, searchVO));
    }
}
