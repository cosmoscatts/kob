package com.kob.backend.biz.pk;

import org.springframework.stereotype.Service;

import com.kob.backend.consumer.WebSocketServer;

@Service
public class StartGameBizImpl implements StartGameBiz {
    @Override
    public void startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        WebSocketServer.startGame(aId, aBotId, bId, bBotId, "match");
    }
}
