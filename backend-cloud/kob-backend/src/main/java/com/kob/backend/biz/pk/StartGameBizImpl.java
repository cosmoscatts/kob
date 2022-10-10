package com.kob.backend.biz.pk;

import com.kob.backend.consumer.WebSocketServer;

public class StartGameBizImpl implements StartGameBiz {
    @Override
    public void startGame(Integer aId, Integer bId) {
        WebSocketServer.startGame(aId, bId);
    }
}
