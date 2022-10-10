package com.kob.backend.biz.pk;

import org.springframework.stereotype.Service;

import com.kob.backend.consumer.WebSocketServer;

@Service
public class StartGameBizImpl implements StartGameBiz {
    @Override
    public void startGame(Integer aId, Integer bId) {
        WebSocketServer.startGame(aId, bId);
    }
}
