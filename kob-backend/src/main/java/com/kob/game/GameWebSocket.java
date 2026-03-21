package com.kob.game;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kob.game.GameEngine;
import com.kob.security.JwtAuthentication;
import com.kob.game.Player;
import com.kob.model.entity.Bot;
import com.kob.model.entity.User;
import com.kob.service.BotService;
import com.kob.service.RankService;
import com.kob.service.RecordService;
import com.kob.service.UserService;
import com.kob.game.bot.BotPool;
import com.kob.game.matching.MatchingPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint("/websocket/{token}")
public class GameWebSocket {
    /**
     * 用户和 websocket server 的映射
     */
    public final static ConcurrentHashMap<Integer, GameWebSocket> users = new ConcurrentHashMap<>();
    public static RecordService recordService;
    public static UserService userService;
    public static RankService rankService;
    public static BotService botService;
    public static MatchingPool matchingPool;
    public static BotPool botPool;
    public GameEngine game;
    private Session session;
    private User user;

    public static void createGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId, String mode) {
        int realBId = bId;
        if ("machine".equals(mode))
            realBId = 1;
        else if ("selfTrain".equals(mode))
            realBId = aId;

        User a = userService.getById(aId), b = userService.getById(realBId);
        Bot botA = botService.getById(aBotId), botB = botService.getById(bBotId);

        GameEngine game = new GameEngine(13, 14, 20, aId, botA, bId, botB, mode);
        game.createMap();

        if (users.get(a.getId()) != null)
            users.get(a.getId()).game = game;
        if (users.get(b.getId()) != null)
            users.get(b.getId()).game = game;

        // 改为前端控制
        // game.start();

        JSONObject respGame = new JSONObject();
        respGame.put("aId", game.getPlayerA().getId());
        respGame.put("aSx", game.getPlayerA().getSx());
        respGame.put("aSy", game.getPlayerA().getSy());
        respGame.put("bId", game.getPlayerB().getId());
        respGame.put("bSx", game.getPlayerB().getSx());
        respGame.put("bSy", game.getPlayerB().getSy());
        respGame.put("map", game.getG());

        JSONObject respA = new JSONObject(), respB = new JSONObject();

        respA.put("event", "match-success");
        respA.put("opponentName", b.getName());
        respA.put("opponentAvatar", b.getAvatar());
        respA.put("game", respGame);
        if (users.get(a.getId()) != null)
            users.get(a.getId()).sendMessage(respA.toJSONString());

        if ("match".equals(mode)) {
            respB.put("event", "match-success");
            respB.put("opponentName", a.getName());
            respB.put("opponentAvatar", a.getAvatar());
            respB.put("game", respGame);
            if (users.get(b.getId()) != null)
                users.get(b.getId()).sendMessage(respB.toJSONString());
        }
    }

    // 匹配动画展示后，真正开始游戏
    public void startGame() {
        if (this.game != null) {
            Player playerA = this.game.getPlayerA();
            if (playerA.getId().equals(this.user.getId())) {
                game.start();
            }
        }
    }

    // JSR-356 WebSocket endpoints are managed per-connection (not Spring singletons),
    // so standard constructor injection does not work. Static setter injection is used
    // to share Spring-managed beans across all WebSocket instances.
    // See: https://jakarta.ee/specifications/websocket/
    @Autowired
    public void setUserService(UserService userService) {
        GameWebSocket.userService = userService;
    }

    @Autowired
    public void setRecordService(RecordService recordService) {
        GameWebSocket.recordService = recordService;
    }

    @Autowired
    public void setRankService(RankService rankService) {
        GameWebSocket.rankService = rankService;
    }

    @Autowired
    public void setBotService(BotService botService) {
        GameWebSocket.botService = botService;
    }

    @Autowired
    public void setMatchingPool(MatchingPool matchingPool) {
        GameWebSocket.matchingPool = matchingPool;
    }

    @Autowired
    public void setBotPool(BotPool botPool) {
        GameWebSocket.botPool = botPool;
    }

    /**
     * 建立连接
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        this.session = session;
        // 根据 token 获取用户 id
        int userId = JwtAuthentication.getUserId(token);
        this.user = userService.getById(userId);

        if (!Objects.isNull(this.user)) {
            users.put(userId, this);
        } else {
            this.session.close();
        }
    }

    /**
     * 关闭链接
     */
    @OnClose
    public void onClose() {
        if (!Objects.isNull(user)) {
            users.remove(user.getId());
        }
    }

    /**
     * 开始匹配 — 直接调用 MatchingPool
     */
    private void startMatching(Integer botId) {
        matchingPool.addPlayer(this.user.getId(), this.user.getRating(), botId);
    }

    /**
     * 结束匹配 — 直接调用 MatchingPool
     */
    private void stopMatching() {
        matchingPool.removePlayer(this.user.getId());
    }

    /**
     * 移动
     */
    private void move(int direction) {
        if (game.getPlayerA().getId().equals(user.getId())) {
            // 亲自出马
            if (game.getPlayerA().getBotId().equals(-1))
                game.setNextStepA(direction);
        } else if (game.getPlayerB().getId().equals(user.getId())) {
            // 亲自出马
            if (game.getPlayerB().getBotId().equals(-1))
                game.setNextStepB(direction);
        }
    }

    /**
     * 从 Client 接收消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject data = JSON.parseObject(message);
        String event = data.getString("event");
        if ("start-matching".equals(event)) {
            startMatching(data.getInteger("botId"));
        } else if ("start-machine-training".equals(event)) {
            // 与人机匹配还是与自己的 bot 匹配
            // 与人机匹配传人机的 id，固定为 1，否则传用户自己的 id
            // mode 有三种：machine | selfTrain (自己打自己) | match
            Integer botId = data.getInteger("botId");
            Integer machineId = data.getInteger("machineId");
            Integer machineBotId = data.getInteger("machineBotId");
            String mode = "selfTrain";
            if (machineId == 1) { // 匹配的是人机，不是玩家的 bot
                mode = "machine";
            }
            String str = new SimpleDateFormat("HHmmss").format(new Date());
            int random = (int)(new Random().nextDouble() * (999 - 100 + 1)) + 100; // 获取 3 位随机数
            machineId = Integer.parseInt((random + str).trim());
            users.put(machineId, this);
            createGame(this.user.getId(), botId, machineId, machineBotId, mode);
        } else if ("stop-matching".equals(event)) {
            stopMatching();
        } else if ("move".equals(event)) {
            move(data.getInteger("direction"));
        } else if ("start-game".equals(event)) {
            startGame();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket error for session {}", session.getId(), error);
    }

    /**
     * 给 Client 发送消息
     */
    public void sendMessage(String message) {
        synchronized (session) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.error("Failed to send WebSocket message", e);
            }
        }
    }
}
