package com.kob.backend.netty;

import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.exception.BusinessException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.AttributeKey;

public class AuthenticationHandler extends ChannelInboundHandlerAdapter {
    private static final AttributeKey<Integer> USER_ID_KEY = AttributeKey.valueOf("userId");
    private boolean authenticationComplete = false;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 不立即触发 channelActive，而是等待认证完成
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            String token = ctx.channel().attr(TokenExtractorHandler.TOKEN_ATTRIBUTE_KEY).get();
            if (token == null || token.isEmpty()) {
                ctx.close();
                return;
            }

            try {
                Integer userId = JwtAuthentication.getUserId(token);
                ctx.channel().attr(USER_ID_KEY).set(userId);
                authenticationComplete = true;

                // 认证完成，现在触发 channelActive
                ctx.fireChannelActive();

                // 移除此处理器
                ctx.pipeline().remove(this);
            } catch (BusinessException e) {
                ctx.close();
            }
        } else {
            ctx.fireUserEventTriggered(evt);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (authenticationComplete) {
            ctx.fireChannelRead(msg);
        }
    }
}

