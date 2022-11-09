package com.kob.backend.controller.rank;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kob.backend.biz.rank.RankBiz;
import com.kob.backend.common.PageMap;
import com.kob.backend.common.PageQuery;
import com.kob.backend.common.Result;
import com.kob.backend.controller.rank.vo.RankRespVO;
import com.kob.backend.controller.record.vo.RecordSearchVO;

@RestController
@RequestMapping("/api/rank")
public class RankController {
    @Resource
    private RankBiz rankBiz;

    @GetMapping("/list")
    public Result<PageMap<RankRespVO>> getList(PageQuery pageQuery, RecordSearchVO searchVO) {
        return Result.success(rankBiz.getList(pageQuery, searchVO));
    }
}
