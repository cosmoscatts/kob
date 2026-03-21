package com.kob.game;

/**
 * 游戏生命周期状态机
 */
public enum GameState {
    /** 等待双方就绪 */
    WAITING,
    /** 游戏进行中 */
    PLAYING,
    /** 游戏结束 */
    FINISHED
}
