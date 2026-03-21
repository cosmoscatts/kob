package com.kob.game;

import com.alibaba.fastjson.JSONObject;
import com.kob.model.entity.Bot;
import com.kob.model.entity.Record;
import com.kob.game.bot.BotTask;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class GameEngine extends Thread {
    private static final int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
    /** 游戏总时长上限 10 分钟 */
    private static final long GAME_TIMEOUT_MS = 10 * 60 * 1000;

    private final String mode;
    private final Integer rows;
    private final Integer cols;
    private final Integer insideRandomWallNum;
    private final int[][] g;
    private final Player playerA;
    private final Player playerB;
    private final ReentrantLock lock = new ReentrantLock();

    private Integer nextStepA;
    private Integer nextStepB;
    private volatile GameState state = GameState.WAITING;
    private String loser;
    private long gameStartTime;

    public GameEngine(Integer rows, Integer cols, Integer insideRandomWallNum, Integer idA, Bot botA, Integer idB,
        Bot botB, String mode) {
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

    public GameState getGameState() {
        return state;
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

    /**
     * 玩家断线处理：断线方判负，立即结束游戏
     */
    public void setPlayerDisconnected(Integer userId) {
        lock.lock();
        try {
            if (state != GameState.PLAYING) return;

            state = GameState.FINISHED;
            if (playerA.getId().equals(userId)) {
                loser = "A";
            } else if (playerB.getId().equals(userId)) {
                loser = "B";
            }
            log.info("玩家断线 [userId={}], 判定 {} 负", userId, loser);
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
        if (player.getBotId().equals(-1))
            return;
        BotTask task = new BotTask(player.getId(), player.getBotCode(), getInput(player), this);
        GameWebSocket.botPool.addBot(task);
    }

    /**
     * 获取两名玩家的下一步操作，最多等待 5 秒
     * 期间检查游戏是否已因断线而结束
     */
    private boolean getNextStep() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            return false;
        }

        sendBotCode(playerA);
        sendBotCode(playerB);

        for (int i = 0; i < 50; i++) {
            // 如果游戏已因断线结束，立即返回
            if (state == GameState.FINISHED) return false;

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
                log.error("等待玩家操作时被中断", e);
                return false;
            }
        }
        return false;
    }

    private boolean checkValid(List<Cell> cellsA, List<Cell> cellsB) {
        int n = cellsA.size();
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

    private void judge() {
        List<Cell> cellsA = playerA.getCells(), cellsB = playerB.getCells();

        boolean validA = checkValid(cellsA, cellsB);
        boolean validB = checkValid(cellsB, cellsA);
        if (!validA || !validB) {
            state = GameState.FINISHED;

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
        if (GameWebSocket.users.get(playerA.getId()) != null)
            GameWebSocket.users.get(playerA.getId()).sendMessage(message);
        if ("match".equals(mode)) {
            if (GameWebSocket.users.get(playerB.getId()) != null)
                GameWebSocket.users.get(playerB.getId()).sendMessage(message);
        }
    }

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
        GameWebSocket.rankService.updateUserRating(player.getId(), rating);
    }

    private void saveToDatabase() {
        Integer playerBId = "machine".equals(mode) ? 1 : "match".equals(mode) ? playerB.getId() : playerA.getId();

        Integer ratingA = GameWebSocket.userService.getById(playerA.getId()).getRating();
        Integer ratingB = GameWebSocket.userService.getById(playerBId).getRating();

        if ("match".equals(mode) && !Objects.equals(playerA.getId(), playerB.getId())) {
            int newRatingA = GameWebSocket.rankService.calculateNewRating(ratingA, ratingB, !"A".equals(loser));
            int newRatingB = GameWebSocket.rankService.calculateNewRating(ratingB, ratingA, !"B".equals(loser));

            updateUserRating(playerA, newRatingA);
            updateUserRating(playerB, newRatingB);
        }

        Record record = new Record();
        record.setId(null).setAId(playerA.getId()).setASx(playerA.getSx()).setASy(playerA.getSy()).setBId(playerBId)
            .setBSx(playerB.getSx()).setBSy(playerB.getSy()).setASteps(playerA.getStepsString())
            .setBSteps(playerB.getStepsString()).setMap(getMapString()).setLoser(loser).setCreateTime(new Date())
            .setMode(this.mode);

        GameWebSocket.recordService.save(record);
    }

    private void sendResult() {
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", loser);
        saveToDatabase();
        sendMessage(resp.toJSONString());
    }

    @Override
    public void run() {
        state = GameState.PLAYING;
        gameStartTime = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            // 检查游戏总时长超时
            if (System.currentTimeMillis() - gameStartTime > GAME_TIMEOUT_MS) {
                state = GameState.FINISHED;
                loser = "all";
                log.info("游戏超时（10分钟），判定平局");
                sendResult();
                break;
            }

            // 如果已因断线结束
            if (state == GameState.FINISHED) {
                sendResult();
                break;
            }

            if (getNextStep()) {
                judge();
                if (state == GameState.PLAYING) {
                    sendMove();
                } else {
                    sendResult();
                    break;
                }
            } else {
                // 如果是断线导致的结束，loser 已设置
                if (state == GameState.FINISHED) {
                    sendResult();
                    break;
                }
                // 超时未操作
                state = GameState.FINISHED;
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
