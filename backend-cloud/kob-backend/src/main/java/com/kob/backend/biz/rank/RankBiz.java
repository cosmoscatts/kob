package com.kob.backend.biz.rank;

import com.kob.backend.common.PageMap;
import com.kob.backend.common.PageQuery;
import com.kob.backend.controller.rank.vo.RankRespVO;
import com.kob.backend.controller.record.vo.RecordSearchVO;

public interface RankBiz {
    // 查询排行榜列表
    PageMap<RankRespVO> getList(PageQuery pageQuery, RecordSearchVO searchVO);
}
