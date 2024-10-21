package com.kob.backend.netty;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final AtomicInteger userIdCounter = new AtomicInteger(1);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String userId = "User" + userIdCounter.getAndIncrement();
        channelMap.put(userId, ctx.channel());
        System.out.println("Client connected - " + ctx.channel().remoteAddress() + " (UserId: " + userId + ")");
        broadcastSystemMessage(userId, "进入了聊天室");
        broadcastUserList();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String userId = getUserIdByChannel(ctx.channel());
        if (userId != null) {
            channelMap.remove(userId);
            System.out.println("Client disconnected - " + ctx.channel().remoteAddress() + " (UserId: " + userId + ")");
            broadcastSystemMessage(userId, "离开了聊天室");
            broadcastUserList();
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
        String message = frame.text();
        System.out.println("Received message: " + message);

        ChatMessage chatMessage = objectMapper.readValue(message, ChatMessage.class);
        broadcastChatMessage(chatMessage);
    }

    private void broadcastSystemMessage(String userId, String action) {
        String timestamp = getCurrentTimestamp();
        SystemMessage systemMessage = new SystemMessage(timestamp, userId, action);
        broadcastMessage(systemMessage);
    }

    private void broadcastChatMessage(ChatMessage chatMessage) {
        String timestamp = getCurrentTimestamp();
        chatMessage.setTimestamp(timestamp);
        broadcastMessage(chatMessage);
    }

    private void broadcastMessage(Object message) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            TextWebSocketFrame frame = new TextWebSocketFrame(jsonMessage);
            channelMap.values().forEach(channel -> channel.writeAndFlush(frame.retain()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void broadcastUserList() {
        List<String> userList = new ArrayList<>(channelMap.keySet());
        UserListMessage userListMessage = new UserListMessage(userList);
        broadcastMessage(userListMessage);
    }

    private String getUserIdByChannel(Channel channel) {
        return channelMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(channel))
                .map(java.util.Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}

@Data
class ChatMessage {
    private String type = "CHAT";
    private String userId;
    private String content;
    private String timestamp;
}

@Data
class SystemMessage {
    private String type = "SYSTEM";
    private String timestamp;
    private String userId;
    private String action;

    public SystemMessage(String timestamp, String userId, String action) {
        this.timestamp = timestamp;
        this.userId = userId;
        this.action = action;
    }
}

@Data
class UserListMessage {
    private String type = "USER_LIST";
    private List<String> users;

    public UserListMessage(List<String> users) {
        this.users = users;
    }
}
