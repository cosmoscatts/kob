package com.kob.backend.service;

public interface RatingService {
    double calculateNewRating(int playerRating, int opponentRating, boolean won);
}
