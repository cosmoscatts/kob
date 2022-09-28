package com.kob.backend.consumer.utils;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Player {
    /** id */
    private Integer id;
    /** 玩家所在的起始行 */
    private Integer sx;
    /** 玩家所在的起始列 */
    private Integer sy;
    /** 记录玩家的所有指令 */
    private List<Integer> steps;
}
