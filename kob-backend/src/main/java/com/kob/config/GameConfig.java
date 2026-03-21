package com.kob.config;

import com.kob.game.bot.BotPool;
import com.kob.game.bot.BotSecurityManager;
import com.kob.game.matching.MatchingPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class GameConfig {

    @Bean
    public MatchingPool matchingPool() {
        MatchingPool pool = new MatchingPool();
        pool.setDaemon(true);
        pool.start();
        return pool;
    }

    @Bean
    public BotPool botPool() {
        BotPool pool = new BotPool();
        pool.setDaemon(true);
        pool.start();
        return pool;
    }

    @PostConstruct
    public void initSecurityManager() {
        System.setSecurityManager(new BotSecurityManager());
    }
}
