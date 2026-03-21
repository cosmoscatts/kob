package com.kob.game.matching;

import com.kob.game.GameWebSocket;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class MatchingPool extends Thread {
    private static List<MatchingPlayer> players = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();

    private void increaseWaitingTime() {
        for (MatchingPlayer player : players) {
            player.setWaitingTime(player.getWaitingTime() + 1);
        }
    }

    private boolean checkMatched(MatchingPlayer a, MatchingPlayer b) {
        int ratingDelta = Math.abs(a.getRating() - b.getRating());
        int waitingTime = Math.min(a.getWaitingTime(), b.getWaitingTime());
        return !Objects.equals(a.getUserId(), b.getUserId()) && ratingDelta <= waitingTime * 10;
    }

    private void matchPlayers() {
        int n = players.size();
        boolean[] used = new boolean[n];
        for (int i = 0; i < n; i++) {
            if (used[i])
                continue;
            for (int j = i + 1; j < n; j++) {
                if (used[j])
                    continue;
                MatchingPlayer a = players.get(i), b = players.get(j);
                if (checkMatched(a, b)) {
                    used[i] = used[j] = true;
                    sendResult(a, b);
                    break;
                }
            }
        }

        List<MatchingPlayer> newPlayers = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!used[i]) {
                newPlayers.add(players.get(i));
            }
        }
        players = newPlayers;
    }

    /**
     * 匹配成功，直接创建游戏
     */
    private void sendResult(MatchingPlayer a, MatchingPlayer b) {
        GameWebSocket.createGame(a.getUserId(), a.getBotId(), b.getUserId(), b.getBotId(), "match");
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
                log.error("MatchingPool interrupted", e);
                break;
            }
        }
    }

    public void addPlayer(Integer userId, Integer rating, Integer botId) {
        lock.lock();
        try {
            players.add(new MatchingPlayer(userId, rating, botId, 0));
        } finally {
            lock.unlock();
        }
    }

    public void removePlayer(Integer userId) {
        lock.lock();
        try {
            List<MatchingPlayer> newPlayers = new ArrayList<>();
            for (MatchingPlayer player : players) {
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
