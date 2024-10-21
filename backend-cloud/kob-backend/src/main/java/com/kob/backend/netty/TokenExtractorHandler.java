package com.kob.backend.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.AttributeKey;

public class TokenExtractorHandler extends ChannelInboundHandlerAdapter {

    public static final AttributeKey<String> TOKEN_ATTRIBUTE_KEY = AttributeKey.valueOf("token");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();

            String[] parts = uri.split("/");
            if (parts.length >= 3) {
                String token = parts[2];
                ctx.channel().attr(TOKEN_ATTRIBUTE_KEY).set(token);
                request.setUri("/chat");
            }
        }
        ctx.fireChannelRead(msg);
    }
}

