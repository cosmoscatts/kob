package com.kob.backend.consumer.utils;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.dataobject.BotDO;
import com.kob.backend.dataobject.RecordDO;
import com.kob.backend.dataobject.UserDO;

public class Game extends Thread {
    private final static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
    private final static String ADD_BOT_URL = "http://127.0.0.1:3002/bot/add/";
    // 当前对局的模式：匹配、人机
    private final String mode; // machine / selfTrain / match
    private final Integer rows;
    private final Integer cols;
    private final Integer insideRandomWallNum;
    private final int[][] g;
    private final Player playerA;
    private final Player playerB;
    private final ReentrantLock lock = new ReentrantLock();
    // 玩家 A 的下一步操作
    private Integer nextStepA;
    // 玩家 B 的下一步操作
    private Integer nextStepB;
    // ['playing', 'finished']
    private String status = "playing";
    // ['all', 'A', 'B']
    private String loser;

    public Game(Integer rows, Integer cols, Integer insideRandomWallNum, Integer idA, BotDO botA, Integer idB,
        BotDO botB, String mode) {
        this.rows = rows;
        this.cols = cols;
        this.insideRandomWallNum = insideRandomWallNum;
        this.g = new int[rows][cols];

        Integer botIdA = -1, botIdB = -1;
        String botCodeA = "", botCodeB = "";
        if (botA != null) {
            botIdA = botA.getId();
            botCodeA = botA.getContent();
        }
        if (botB != null) {
            botIdB = botB.getId();
            botCodeB = botB.getContent();
        }

        this.playerA = new Player(idA, botIdA, botCodeA, rows - 2, 1, new ArrayList<>());
        this.playerB = new Player(idB, botIdB, botCodeB, 1, cols - 2, new ArrayList<>());

        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    public int[][] getG() {
        return g;
    }

    public Player getPlayerA() {
        return playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }

    public Integer getNextStepA() {
        return nextStepA;
    }

    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }
    }

    public Integer getNextStepB() {
        return nextStepB;
    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
    }

    private boolean checkConnectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty)
            return true;
        g[sx][sy] = 1;

        for (int i = 0; i < 4; i++) {
            int x = sx + dx[i], y = sy + dy[i];
            if (x >= 0 && x < this.rows && y >= 0 && y < this.cols && g[x][y] == 0) {
                if (checkConnectivity(x, y, tx, ty)) {
                    g[sx][sy] = 0;
                    return true;
                }
            }
        }

        g[sx][sy] = 0;
        return false;
    }

    /**
     * 画地图
     */
    private boolean draw() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                g[i][j] = 0;
            }
        }

        for (int r = 0; r < this.rows; r++) {
            g[r][0] = g[r][this.cols - 1] = 1;
        }
        for (int c = 0; c < this.cols; c++) {
            g[0][c] = g[this.rows - 1][c] = 1;
        }

        Random random = new Random();
        for (int i = 0; i < this.insideRandomWallNum / 2; i++) {
            for (int j = 0; j < 1000; j++) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);

                if (g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1)
                    continue;
                if (r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2)
                    continue;

                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }

        return checkConnectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    public void createMap() {
        for (int i = 0; i < 1000; i++) {
            if (draw())
                break;
        }
    }

    /**
     * 将当前局面信息编码成字符串 地图 # meSx # meSy # 我的操作 # youSx # youSy # 对手操作
     */
    private String getInput(Player player) {
        Player me, you;
        if (playerA.getId().equals(player.getId())) {
            me = playerA;
            you = playerB;
        } else {
            me = playerB;
            you = playerA;
        }
        return getMapString() + "#" + me.getSx() + "#" + me.getSy() + "#(" + me.getStepsString() + ")#" + you.getSx()
            + "#" + you.getSy() + "#(" + you.getStepsString() + ")";
    }

    private void sendBotCode(Player player) {
        // 亲自出马
        if (player.getBotId().equals(-1))
            return;
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("userId", player.getId().toString());
        data.add("botCode", player.getBotCode());
        data.add("input", getInput(player));
        WebSocketServer.restTemplate.postForObject(ADD_BOT_URL, data, String.class);
    }

    /**
     * 获取两名玩家的下一步操作
     */
    private boolean getNextStep() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        sendBotCode(playerA);
        sendBotCode(playerB);

        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(100);
                lock.lock();
                try {
                    if (nextStepA != null && nextStepB != null) {
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 检测目标位置是否合法，未撞到两条蛇的身体或者墙
     */
    private boolean checkValid(List<Cell> cellsA, List<Cell> cellsB) {
        int n = cellsA.size();
        // A 的蛇头
        Cell cell = cellsA.get(n - 1);
        if (g[cell.x][cell.y] == 1)
            return false;

        for (int i = 0; i < n - 1; i++) {
            if (cellsA.get(i).x == cell.x && cellsA.get(i).y == cell.y)
                return false;
        }

        for (int i = 0; i < n - 1; i++) {
            if (cellsB.get(i).x == cell.x && cellsB.get(i).y == cell.y)
                return false;
        }

        return true;
    }

    /**
     * 判断两名玩家下一步操作是否合法
     */
    private void judge() {
        List<Cell> cellsA = playerA.getCells(), cellsB = playerB.getCells();

        boolean validA = checkValid(cellsA, cellsB);
        boolean validB = checkValid(cellsB, cellsA);
        if (!validA || !validB) {
            status = "finished";

            if (!validA && !validB) {
                loser = "all";
            } else if (!validA) {
                loser = "A";
            } else {
                loser = "B";
            }
        }
    }

    private void sendMessage(String message) {
        if (WebSocketServer.users.get(playerA.getId()) != null)
            WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        if ("match".equals(mode)) {
            if (WebSocketServer.users.get(playerB.getId()) != null)
                WebSocketServer.users.get(playerB.getId()).sendMessage(message);
        }
    }

    /**
     * 向客户端返回两名玩家操作信息
     */
    private void sendMove() {
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("aDirection", nextStepA);
            resp.put("bDirection", nextStepB);
            sendMessage(resp.toJSONString());
            nextStepA = nextStepB = null;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 将地图转为字符串保存
     */
    private String getMapString() {
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                ans.append(g[i][j]);
            }
        }
        return ans.toString();
    }

    private void updateUserRating(Player player, Integer rating) {
        WebSocketServer.userService
            .update(Wrappers.<UserDO>lambdaUpdate().eq(UserDO::getId, player.getId()).set(UserDO::getRating, rating));
    }

    private void saveToDatabase() {
        // 只有匹配模式，playerB 才是真实 id
        Integer playerBId = "machine".equals(mode) ? 1 : "match".equals(mode) ? playerB.getId() : playerA.getId();

        Integer ratingA = WebSocketServer.userService.getById(playerA.getId()).getRating();
        Integer ratingB = WebSocketServer.userService.getById(playerBId).getRating();

        if ("match".equals(mode) && !Objects.equals(playerA.getId(), playerB.getId())) {
            if ("A".equals(loser)) {
                ratingA -= 2;
                ratingB += 5;
            } else if ("B".equals(loser)) {
                ratingA += 5;
                ratingB -= 2;
            }

            updateUserRating(playerA, ratingA);
            updateUserRating(playerB, ratingB);
        }

        RecordDO record = new RecordDO();
        record.setId(null).setAId(playerA.getId()).setASx(playerA.getSx()).setASy(playerA.getSy()).setBId(playerBId)
            .setBSx(playerB.getSx()).setBSy(playerB.getSy()).setASteps(playerA.getStepsString())
            .setBSteps(playerB.getStepsString()).setMap(getMapString()).setLoser(loser).setCreateTime(new Date())
            .setMode(this.mode);

        WebSocketServer.recordService.save(record);
    }

    /**
     * 向两名玩家发送对局结果
     */
    private void sendResult() {
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", loser);
        saveToDatabase();
        sendMessage(resp.toJSONString());
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            // 判断是否获取两名玩家的下一步操作
            if (getNextStep()) {
                judge();
                if ("playing".equals(status)) {
                    sendMove();
                } else {
                    sendResult();
                    break;
                }
            } else {
                // 游戏结束
                status = "finished";
                lock.lock();
                try {
                    if (nextStepA == null && nextStepB == null) {
                        loser = "all";
                    } else if (nextStepA == null) {
                        loser = "A";
                    } else {
                        loser = "B";
                    }
                } finally {
                    lock.unlock();
                }
                sendResult();
                break;
            }
        }
    }
}
