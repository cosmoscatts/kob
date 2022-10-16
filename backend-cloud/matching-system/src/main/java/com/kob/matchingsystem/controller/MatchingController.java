package com.kob.matchingsystem.controller;

import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kob.matchingsystem.service.MatchingService;

@RestController
@RequestMapping("/player")
public class MatchingController {
    @Resource
    private MatchingService matchingService;

    @PostMapping("/add/")
    public String addPlayer(@RequestParam MultiValueMap<String, String> data) {
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("userId")));
        Integer rating = Integer.parseInt(Objects.requireNonNull(data.getFirst("rating")));
        Integer botId = Integer.parseInt(Objects.requireNonNull(data.getFirst("botId")));
        return matchingService.addPlayer(userId, rating, botId);
    }

    @PostMapping("/remove/")
    public String removePlayer(@RequestParam MultiValueMap<String, String> data) {
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("userId")));
        return matchingService.removePlayer(userId);
    }
}
