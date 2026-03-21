package com.kob.service;

import com.kob.common.PageMap;
import com.kob.common.PageQuery;
import com.kob.model.vo.response.RankRespVO;
import com.kob.model.vo.request.RecordSearchVO;

public interface RankService {
    void updateUserRating(Integer userId, Integer newRating);

    // 查询排行榜列表
    PageMap<RankRespVO> getRankingList(PageQuery pageQuery, RecordSearchVO searchVO);

    int calculateNewRating(int playerRating, int opponentRating, boolean won);
}
