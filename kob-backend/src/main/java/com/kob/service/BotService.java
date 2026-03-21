package com.kob.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kob.common.DeleteQuery;
import com.kob.common.PageMap;
import com.kob.common.PageQuery;
import com.kob.model.vo.request.BotReqVO;
import com.kob.model.vo.response.BotRespVO;
import com.kob.model.entity.Bot;

import java.util.List;

public interface BotService extends IService<Bot> {
    // 获取 bot 列表
    PageMap<BotRespVO> getList(PageQuery pageQuery);

    // 获取人机 bot 列表
    List<BotRespVO> getMachineBots();

    // 添加 bot
    String add(BotReqVO botReqVO);

    // 修改 bot
    String update(BotReqVO botReqVO);

    // 删除 bot
    String delete(DeleteQuery query);
}
