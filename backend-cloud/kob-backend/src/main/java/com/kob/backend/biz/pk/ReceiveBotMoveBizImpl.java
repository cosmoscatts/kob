package com.kob.backend.biz.pk;

import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.consumer.utils.Game;
import org.springframework.stereotype.Service;

@Service
public class ReceiveBotMoveBizImpl implements ReceiveBotMoveBiz {
    @Override
    public String receiveBotMove(Integer userId, Integer direction) {
        if (WebSocketServer.users.get(userId) != null) {
            Game game = WebSocketServer.users.get(userId).game;
            if (game != null) {
                if ("machine".equals(game.getMode())) {
                    if (game.getPlayerA().getBotId() == -1) {
                        if (game.getPlayerB().getId().equals(userId)) {
                            game.setNextStepB(direction);
                        }
                    } else {
                        Integer nextStepsA = game.getNextStepA();
                        if (nextStepsA != null) {
                            if (game.getPlayerB().getId().equals(userId)) {
                                game.setNextStepB(direction);
                            }
                        } else {
                            if (game.getPlayerA().getId().equals(userId)) {
                                game.setNextStepA(direction);
                            }
                        }
                    }
                } else {
                    if (game.getPlayerA().getId().equals(userId)) {
                        game.setNextStepA(direction);
                    } else if (game.getPlayerB().getId().equals(userId)) {
                        game.setNextStepB(direction);
                    }
                }
            }
        }
        return "receive bot move success";
    }
}