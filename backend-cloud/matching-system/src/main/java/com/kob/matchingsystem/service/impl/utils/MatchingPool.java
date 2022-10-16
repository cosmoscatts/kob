package com.kob.matchingsystem.service.impl.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class MatchingPool extends Thread {
    private final static String START_GAME_URL = "http://127.0.0.1:3000/pk/start/game";
    private static List<Player> players = new ArrayList<>();
    private static RestTemplate restTemplate;
    private final ReentrantLock lock = new ReentrantLock();

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        MatchingPool.restTemplate = restTemplate;
    }

    /**
     * 将所以待匹配玩家等待时间加 1
     */
    private void increaseWaitingTime() {
        for (Player player : players) {
            player.setWaitingTime(player.getWaitingTime() + 1);
        }
    }

    /**
     * 判断两名玩家是否匹配
     */
    private boolean checkMatched(Player a, Player b) {
        int ratingDelta = Math.abs(a.getRating() - b.getRating());
        int waitingTime = Math.min(a.getWaitingTime(), b.getWaitingTime());
        return ratingDelta <= waitingTime * 10;
    }

    /**
     * 尝试匹配所有玩家
     */
    private void matchPlayers() {
        int n = players.size();
        boolean[] used = new boolean[n];
        for (int i = 0; i < n; i++) {
            if (used[i])
                continue;
            for (int j = i + 1; j < n; j++) {
                if (used[j])
                    continue;
                Player a = players.get(i), b = players.get(j);
                if (checkMatched(a, b)) {
                    used[i] = used[j] = true;
                    sendResult(a, b);
                    break;
                }
            }
        }

        List<Player> newPlayers = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!used[i]) {
                newPlayers.add(players.get(i));
            }
        }
        players = newPlayers;
    }

    /**
     * 返回匹配结果
     */
    private void sendResult(Player a, Player b) {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("aId", a.getUserId().toString());
        data.add("aBotId", a.getBotId().toString());
        data.add("bId", b.getUserId().toString());
        data.add("bBotId", b.getBotId().toString());
        restTemplate.postForObject(START_GAME_URL, data, String.class);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                lock.lock();
                try {
                    increaseWaitingTime();
                    matchPlayers();
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void addPlayer(Integer userId, Integer rating, Integer botId) {
        lock.lock();
        try {
            players.add(new Player(userId, rating, botId, 0));
        } finally {
            lock.unlock();
        }
    }

    public void removePlayer(Integer userId) {
        lock.lock();
        try {
            List<Player> newPlayers = new ArrayList<>();
            for (Player player : players) {
                if (!player.getUserId().equals(userId)) {
                    newPlayers.add(player);
                }
            }
            players = newPlayers;
        } finally {
            lock.unlock();
        }
    }

}
