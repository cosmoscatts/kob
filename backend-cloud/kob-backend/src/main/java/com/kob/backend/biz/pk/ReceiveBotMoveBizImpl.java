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
                if ("match".equals(game.getMode())) {
                    handleMatchMove(game, userId, direction);
                } else {
                    handleMachineOrSelfTrainMove(game, userId, direction);
                }
            }
        }
        return "receive bot move success";
    }

    private void handleMachineOrSelfTrainMove(Game game, Integer userId, Integer direction) {
        if (game.getPlayerA().getBotId() == -1) { // 亲自出马
            if (game.getPlayerB().getId().equals(userId)) {
                game.setNextStepB(direction);
                return;
            }
        }
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

    private void handleMatchMove(Game game, Integer userId, Integer direction) {
        if (game.getPlayerA().getId().equals(userId)) {
            game.setNextStepA(direction);
        } else if (game.getPlayerB().getId().equals(userId)) {
            game.setNextStepB(direction);
        }
    }
}