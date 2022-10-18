package com.kob.backend.biz.user;

import com.kob.backend.common.DeleteQuery;
import com.kob.backend.common.PageMap;
import com.kob.backend.common.PageQuery;
import com.kob.backend.controller.user.vo.BotReqVO;
import com.kob.backend.controller.user.vo.BotRespVO;

public interface UserBotBiz {
    // 获取 bot 列表
    PageMap<BotRespVO> getList(PageQuery pageQuery);

    // 添加 bot
    String add(BotReqVO botReqVO);

    // 修改 bot
    String update(BotReqVO botReqVO);

    // 删除 bot
    String delete(DeleteQuery query);
}
