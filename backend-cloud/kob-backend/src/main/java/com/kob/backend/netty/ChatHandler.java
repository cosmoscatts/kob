package com.kob.backend.netty;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.Data;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final AttributeKey<Integer> USER_ID_KEY = AttributeKey.valueOf("userId");

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Integer userId = ctx.channel().attr(USER_ID_KEY).get();
        if (userId != null) {
            channelMap.put(String.valueOf(userId), ctx.channel());
            System.out.println("Client connected - " + ctx.channel().remoteAddress() + " (UserId: " + userId + ")");
            broadcastSystemMessage(String.valueOf(userId), "进入了聊天室");
            broadcastUserList();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
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
        String userId = getUserIdByChannel(ctx.channel());
        if (userId != null) {
            String message = frame.text();
            System.out.println("Received message: " + message);

            ChatMessage chatMessage = objectMapper.readValue(message, ChatMessage.class);
            chatMessage.setUserId(userId);
            broadcastChatMessage(chatMessage);
        }
    }

    private void broadcastSystemMessage(String userId, String action) {
        String timestamp = getCurrentTimestamp();
        SystemMessage systemMessage = new SystemMessage(userId, timestamp, action);
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
            byte[] bytes = jsonMessage.getBytes(StandardCharsets.UTF_8);
            channelMap.values().forEach(channel -> {
                if (channel.isActive()) {
                    TextWebSocketFrame frame = new TextWebSocketFrame(Unpooled.wrappedBuffer(bytes));
                    channel.writeAndFlush(frame);
                }
            });
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
    private String userId;
    private String timestamp;
    private String action;

    public SystemMessage(String userId, String timestamp, String action) {
        this.userId = userId;
        this.timestamp = timestamp;
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
