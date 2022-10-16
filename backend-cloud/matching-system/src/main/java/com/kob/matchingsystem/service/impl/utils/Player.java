package com.kob.matchingsystem.service.impl.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Player {
    private Integer userId;
    private Integer rating;
    /** 出战的 bot */
    private Integer botId;
    /** 等待时间 */
    private Integer waitingTime;
}
