package com.kob.backend.consumer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.consumer.utils.Player;
import com.kob.backend.dataobject.BotDO;
import com.kob.backend.dataobject.UserDO;
import com.kob.backend.service.BotService;
import com.kob.backend.service.RecordService;
import com.kob.backend.service.UserService;

@Component
@ServerEndpoint("/websocket/{token}")
public class WebSocketServer {
    /**
     * 用户和 websocket server 的映射
     */
    public final static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();
    private final static String ADD_PLAYER_URL = "http://127.0.0.1:3001/player/add/";
    private final static String REMOVE_PLAYER_URL = "http://127.0.0.1:3001/player/remove/";
    public static RecordService recordService;
    public static UserService userService;
    public static BotService botService;
    public static RestTemplate restTemplate;
    public Game game;
    private Session session;
    private UserDO user;

    public static void createGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId, String mode) {
        int realBId = bId;
        if ("machine".equals(mode))
            realBId = 1;
        else if ("selfTrain".equals(mode))
            realBId = aId;

        UserDO a = userService.getById(aId), b = userService.getById(realBId);
        BotDO botA = botService.getById(aBotId), botB = botService.getById(bBotId);

        Game game = new Game(13, 14, 20, aId, botA, bId, botB, mode);
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

    // Spring 单例与 Websocket 冲突
    // 这里使用 setter 进行注入
    @Autowired
    public void setUserService(UserService userService) {
        WebSocketServer.userService = userService;
    }

    @Autowired
    public void setRecordService(RecordService recordService) {
        WebSocketServer.recordService = recordService;
    }

    @Autowired
    public void setBotService(BotService botService) {
        WebSocketServer.botService = botService;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        WebSocketServer.restTemplate = restTemplate;
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
     * 开始匹配
     */
    private void startMatching(Integer botId) {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("userId", this.user.getId().toString());
        data.add("rating", this.user.getRating().toString());
        data.add("botId", botId.toString());
        restTemplate.postForObject(ADD_PLAYER_URL, data, String.class);
    }

    /**
     * 结束匹配
     */
    private void stopMatching() {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("userId", this.user.getId().toString());
        restTemplate.postForObject(REMOVE_PLAYER_URL, data, String.class);
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
                List<BotDO> list = botService.list(Wrappers.<BotDO>lambdaQuery().eq(BotDO::getUserId, machineId));
                machineBotId = list.get(machineBotId).getId();
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
        error.printStackTrace();
    }

    /**
     * 给 Client 发送消息
     */
    public void sendMessage(String message) {
        synchronized (session) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
