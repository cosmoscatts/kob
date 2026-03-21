package com.kob.game.bot;

import com.kob.game.GameEngine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BotTask {
    private Integer userId;
    private String botCode;
    private String input;
    /** 关联的游戏实例，用于直接设置下一步 */
    private GameEngine game;
}
