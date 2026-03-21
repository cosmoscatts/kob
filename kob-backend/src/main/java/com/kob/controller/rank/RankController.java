package com.kob.controller.rank;

import com.kob.common.PageMap;
import com.kob.common.PageQuery;
import com.kob.common.Result;
import com.kob.model.vo.response.RankRespVO;
import com.kob.model.vo.request.RecordSearchVO;
import com.kob.service.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rank")
@RequiredArgsConstructor
public class RankController {
    private final RankService rankService;

    @GetMapping("/list")
    public Result<PageMap<RankRespVO>> getList(PageQuery pageQuery, RecordSearchVO searchVO) {
        return Result.success(rankService.getRankingList(pageQuery, searchVO));
    }
}
