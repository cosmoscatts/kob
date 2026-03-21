package com.kob.game.bot;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class BotPool extends Thread {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private final Queue<BotTask> tasks = new LinkedList<>();

    public void addBot(BotTask task) {
        lock.lock();
        try {
            tasks.add(task);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    private void consume(BotTask task) {
        BotExecutor consumer = new BotExecutor();
        consumer.startTimeout(2000, task);
    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            if (tasks.isEmpty()) {
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    log.error("BotPool interrupted", e);
                    lock.unlock();
                    break;
                }
            } else {
                BotTask task = tasks.remove();
                lock.unlock();
                consume(task);
            }
        }
    }
}
