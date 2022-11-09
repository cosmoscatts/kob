package com.kob.backend.biz.record;

import com.kob.backend.common.DeleteQuery;
import com.kob.backend.common.PageMap;
import com.kob.backend.common.PageQuery;
import com.kob.backend.controller.record.vo.RecordRespVO;
import com.kob.backend.controller.record.vo.RecordSearchVO;

public interface RecordBiz {
    // 查询对局列表
    PageMap<RecordRespVO> getList(PageQuery pageQuery, RecordSearchVO searchVO);

    // 删除 bot
    String delete(DeleteQuery query);
}