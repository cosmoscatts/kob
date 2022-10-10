package com.kob.backend.consumer.utils;

import java.util.ArrayList;
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

    /**
     * 检查当前回合蛇的长度是否增加： 前十回合，每回合增加 `1` | 后面回合，每 `3` 回合增加 `1`
     */
    private boolean checkTailIncreasing(int step) {
        if (step <= 10)
            return true;
        return step % 3 == 1;
    }

    public List<Cell> getCells() {
        List<Cell> ans = new ArrayList<>();

        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        int step = 0;
        ans.add(new Cell(x, y));
        for (int d : steps) {
            x += dx[d];
            y += dy[d];
            ans.add(new Cell(x, y));
            if (!checkTailIncreasing(++step)) {
                ans.remove(0);
            }
        }
        return ans;
    }

    /**
     * 将玩家路径转为字符串保存
     */
    public String getStepsString() {
        StringBuilder ans = new StringBuilder();
        for (int d : steps) {
            ans.append(d);
        }
        return ans.toString();
    }
}
