package com.kob.backend.biz.pk;

import org.springframework.stereotype.Service;

import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.consumer.utils.Game;

@Service
public class ReceiveBotMoveBizImpl implements ReceiveBotMoveBiz {
    @Override
    public String receiveBotMove(Integer userId, Integer direction) {
        if (WebSocketServer.users.get(userId) != null) {
            Game game = WebSocketServer.users.get(userId).game;
            if (game != null) {
                if (game.getPlayerA().getId().equals(userId)) {
                    game.setNextStepA(direction);
                } else if (game.getPlayerB().getId().equals(userId)) {
                    game.setNextStepB(direction);
                }
            }
        }

        return "receive bot move success";
    }
}
