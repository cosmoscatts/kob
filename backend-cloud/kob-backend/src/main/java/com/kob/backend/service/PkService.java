package com.kob.backend.service;

public interface PkService {
    void startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId);

    String receiveBotMove(Integer userId, Integer direction);
}
