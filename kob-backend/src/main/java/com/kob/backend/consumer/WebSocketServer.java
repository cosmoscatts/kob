package com.kob.backend.consumer;

import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.dataobject.UserDO;
import com.kob.backend.service.UserService;

@Component
@ServerEndpoint("/websocket/{token}")
public class WebSocketServer {
    /** 用户和 websocket server 的映射 */
    public final static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();
    /** 用户匹配池 */
    private final static CopyOnWriteArraySet<UserDO> matchPool = new CopyOnWriteArraySet<>();
    private static UserService userService;
    private Session session;
    private UserDO user;

    private Game game;

    // Spring 单例与 Websocket 冲突
    @Autowired
    public void setUserService(UserService userService) {
        WebSocketServer.userService = userService;
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
            matchPool.remove(user);
        }
    }

    /**
     * 开始匹配
     */
    private void startMatching() {
        matchPool.add(user);

        while (matchPool.size() >= 2) {
            Iterator<UserDO> it = matchPool.iterator();
            UserDO a = it.next(), b = it.next();
            matchPool.remove(a);
            matchPool.remove(b);

            Game game = new Game(13, 14, 20, a.getId(), b.getId());
            game.createMap();

            users.get(a.getId()).game = game;
            users.get(b.getId()).game = game;

            game.start();

            JSONObject respGame = new JSONObject();
            respGame.put("aId", game.getPlayerA().getId());
            respGame.put("aSx", game.getPlayerA().getSx());
            respGame.put("aSy", game.getPlayerA().getSy());
            respGame.put("bId", game.getPlayerB().getId());
            respGame.put("bSx", game.getPlayerB().getSx());
            respGame.put("bSy", game.getPlayerB().getSy());
            respGame.put("map", game.getG());

            JSONObject respA = new JSONObject();
            respA.put("event", "match-success");
            respA.put("opponentName", b.getName());
            respA.put("opponentAvatar", b.getAvatar());
            respA.put("game", respGame);
            users.get(a.getId()).sendMessage(respA.toJSONString());

            JSONObject respB = new JSONObject();
            respB.put("event", "match-success");
            respB.put("opponentName", a.getName());
            respB.put("opponentAvatar", a.getAvatar());
            respB.put("game", respGame);
            users.get(b.getId()).sendMessage(respB.toJSONString());
        }
    }

    /**
     * 结束匹配
     */
    private void stopMatching() {
        matchPool.remove(user);
    }

    /**
     * 移动
     */
    private void move(int direction) {
        if (game.getPlayerA().getId().equals(user.getId())) {
            game.setNextStepA(direction);
        } else if (game.getPlayerB().getId().equals(user.getId())) {
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
            startMatching();
        } else if ("stop-matching".equals(event)) {
            stopMatching();
        } else if ("move".equals(event)) {
            move(data.getInteger("direction"));
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
