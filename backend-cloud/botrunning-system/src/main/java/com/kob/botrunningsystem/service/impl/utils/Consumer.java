package com.kob.botrunningsystem.service.impl.utils;

import java.util.UUID;

import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.kob.botrunningsystem.utils.BotInterface;

@Component
public class Consumer extends Thread {
    private final static String RECEIVE_BOT_MOVE_URL = "http://127.0.0.1:3000/pk/receive/bot/move/";
    private static RestTemplate restTemplate;
    private Bot bot;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        Consumer.restTemplate = restTemplate;
    }

    public void startTimeout(long timeout, Bot bot) {
        this.bot = bot;
        this.start();
        try {
            this.join(timeout); // 最多等待 timeout 秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.interrupt(); // 中断线程
        }
    }

    /**
     * 在 code 中的 Bot 类名后面添加 uid
     */
    private String addUid(String code, String uid) {
        int k = code.indexOf(" implements com.kob.botrunningsystem.utils.BotInterface");
        return code.substring(0, k) + uid + code.substring(k);
    }

    @Override
    public void run() {
        UUID uuid = UUID.randomUUID();
        String uid = uuid.toString().substring(0, 8);
        BotInterface botInterface =
            Reflect.compile("com.kob.botrunningsystem.utils.Bot" + uid, addUid(bot.getBotCode(), uid)).create().get();
        Integer direction = botInterface.nextMove(bot.getInput());
        System.out.println("Move -> " + bot.getUserId() + " _> " + direction);

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("userId", bot.getUserId().toString());
        data.add("direction", direction.toString());
        restTemplate.postForObject(RECEIVE_BOT_MOVE_URL, data, String.class);
    }
}
