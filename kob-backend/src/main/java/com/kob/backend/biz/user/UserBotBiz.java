package com.kob.backend.biz.user;

import java.util.List;

import com.kob.backend.common.DeleteQuery;
import com.kob.backend.controller.user.vo.BotReqVO;
import com.kob.backend.dataobject.BotDO;

public interface UserBotBiz {
    // 获取 bot 列表
    List<BotDO> getList();

    // 添加 bot
    void add(BotReqVO botReqVO);

    // 修改 bot
    String update();

    // 删除 bot
    String deleteBot(DeleteQuery query);
}
