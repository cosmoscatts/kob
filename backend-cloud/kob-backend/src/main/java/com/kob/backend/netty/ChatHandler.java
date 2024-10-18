package com.kob.backend.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.ArrayList;

public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected - " + ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client disconnected - " + ctx.channel().remoteAddress());
        String userId = getUserIdByChannel(ctx.channel());
        if (userId != null) {
            channelMap.remove(userId);
            broadcastMessage(userId, "left the chat");
            broadcastUserList();
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
        String message = frame.text();
        System.out.println("Received message: " + message);

        ChatMessage chatMessage = objectMapper.readValue(message, ChatMessage.class);

        switch (chatMessage.getType()) {
            case JOIN:
                handleJoinMessage(ctx.channel(), chatMessage);
                break;
            case CHAT:
                handleChatMessage(chatMessage);
                break;
            case LEAVE:
                handleLeaveMessage(ctx.channel(), chatMessage);
                break;
            default:
                System.out.println("Unknown message type");
        }
    }

    private void handleJoinMessage(Channel channel, ChatMessage chatMessage) {
        String userId = chatMessage.getUserId();
        channelMap.put(userId, channel);
        broadcastMessage(userId, "joined the chat");
        broadcastUserList();
    }

    private void handleChatMessage(ChatMessage chatMessage) {
        String message = chatMessage.getUserId() + ": " + chatMessage.getContent();
        broadcastMessage(message);
    }

    private void handleLeaveMessage(Channel channel, ChatMessage chatMessage) {
        String userId = chatMessage.getUserId();
        channelMap.remove(userId);
        broadcastMessage(userId, "left the chat");
        broadcastUserList();
        channel.close();
    }

    private void broadcastMessage(String userId, String action) {
        String message = userId + " " + action;
        broadcastMessage(message);
    }

    private void broadcastMessage(String message) {
        TextWebSocketFrame frame = new TextWebSocketFrame(message);
        channelMap.values().forEach(channel -> channel.writeAndFlush(frame.retain()));
    }

    private void broadcastUserList() {
        List<String> userList = new ArrayList<>(channelMap.keySet());
        try {
            String userListJson = objectMapper.writeValueAsString(new UserListMessage(userList));
            TextWebSocketFrame frame = new TextWebSocketFrame(userListJson);
            channelMap.values().forEach(channel -> channel.writeAndFlush(frame.retain()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUserIdByChannel(Channel channel) {
        return channelMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(channel))
                .map(java.util.Map.Entry::getKey)
                .findFirst()
                .orElse(null);
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
