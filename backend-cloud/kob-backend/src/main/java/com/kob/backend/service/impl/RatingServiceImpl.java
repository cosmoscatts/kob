package com.kob.backend.service.impl;

import com.kob.backend.service.RatingService;
import org.springframework.stereotype.Service;

@Service
public class RatingServiceImpl implements RatingService {
    private static final int K_FACTOR = 32; // 评分调整系数

    public double calculateNewRating(int playerRating, int opponentRating, boolean won) {
        double expectedScore = 1 / (1 + Math.pow(10, (opponentRating - playerRating) / 400.0));
        double actualScore = won ? 1.0 : 0.0;
        return playerRating + K_FACTOR * (actualScore - expectedScore);
    }
}
