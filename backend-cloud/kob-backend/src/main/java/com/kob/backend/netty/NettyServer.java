package com.kob.backend.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

@Component
public class NettyServer {

    @Resource
    private ServerBootstrap serverBootstrap;

    @Value("${netty.port}")
    private int port;

    private Channel serverChannel;

    @PostConstruct
    public void start() throws Exception {
        ChannelFuture future = serverBootstrap.bind(port).sync();
        serverChannel = future.channel();
        System.out.println("Netty server started on port " + port);
    }

    @PreDestroy
    public void stop() {
        if (serverChannel != null) {
            serverChannel.close();
            serverChannel.parent().close();
        }
    }
}
