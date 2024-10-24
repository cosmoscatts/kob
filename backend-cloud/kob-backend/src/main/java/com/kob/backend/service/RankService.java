package com.kob.backend.service;

import com.kob.backend.common.PageMap;
import com.kob.backend.common.PageQuery;
import com.kob.backend.controller.rank.vo.RankRespVO;
import com.kob.backend.controller.record.vo.RecordSearchVO;

public interface RankService {
    void updateUserRating(Integer userId, Integer newRating);

    // 查询排行榜列表
    PageMap<RankRespVO> getRankingList(PageQuery pageQuery, RecordSearchVO searchVO);

    int calculateNewRating(int playerRating, int opponentRating, boolean won);
}
