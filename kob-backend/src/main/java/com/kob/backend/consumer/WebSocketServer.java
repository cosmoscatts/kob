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
    private final ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();
    /** 用户匹配池 */
    private final CopyOnWriteArraySet<UserDO> matchPool = new CopyOnWriteArraySet<>();
    private Session session;
    private UserDO user;
    private UserService userService;

    // Spring 单例与 Websocket 冲突
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
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

            Game game = new Game(13, 14, 20);
            game.createMap();

            JSONObject respA = new JSONObject();
            respA.put("event", "match-success");
            respA.put("opponentName", b.getName());
            respA.put("opponentAvatar", b.getAvatar());
            respA.put("gameMap", game.getG());
            users.get(a.getId()).sendMessage(respA.toJSONString());

            JSONObject respB = new JSONObject();
            respB.put("event", "match-success");
            respB.put("opponentName", a.getName());
            respB.put("opponentAvatar", a.getAvatar());
            respB.put("gameMap", game.getG());
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
     * 从 Client 接收消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject data = JSON.parseObject(message);
        String event = data.getString("event");
        if ("start-matching".equals(event)) {
            startMatching();
        } else if ("end-matching".equals(event)) {
            stopMatching();
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
