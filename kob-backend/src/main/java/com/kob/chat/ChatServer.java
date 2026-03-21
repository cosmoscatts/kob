package com.kob.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatServer {

    private final ServerBootstrap serverBootstrap;

    @Value("${netty.port}")
    private int port;

    private Channel serverChannel;

    @PostConstruct
    public void start() throws Exception {
        ChannelFuture future = serverBootstrap.bind(port).sync();
        serverChannel = future.channel();
        log.info("Netty server started on port {}", port);
    }

    @PreDestroy
    public void stop() {
        if (serverChannel != null) {
            serverChannel.close();
            serverChannel.parent().close();
        }
    }
}
