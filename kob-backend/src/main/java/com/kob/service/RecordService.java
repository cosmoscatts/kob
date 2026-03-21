package com.kob.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kob.common.DeleteQuery;
import com.kob.common.PageMap;
import com.kob.common.PageQuery;
import com.kob.model.vo.response.RecordRespVO;
import com.kob.model.vo.request.RecordSearchVO;
import com.kob.model.entity.Record;

public interface RecordService extends IService<Record> {
    // 查询对局列表
    PageMap<RecordRespVO> getList(PageQuery pageQuery, RecordSearchVO searchVO);

    // 删除 bot
    String delete(DeleteQuery query);
}
