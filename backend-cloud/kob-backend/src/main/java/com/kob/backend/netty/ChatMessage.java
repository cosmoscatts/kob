package com.kob.backend.netty;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ChatMessage {
    public enum MessageType {
        JOIN,
        LEAVE,
        CHAT,
    }

    private MessageType type;
    private String userId;
    private String content;
}
