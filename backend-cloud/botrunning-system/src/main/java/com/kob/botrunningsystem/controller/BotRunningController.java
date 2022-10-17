package com.kob.botrunningsystem.controller;

import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kob.botrunningsystem.service.BotRunningService;

@RestController
@RequestMapping("/bot")
public class BotRunningController {
    @Resource
    private BotRunningService botRunningService;

    @PostMapping("/add/")
    public String addBot(@RequestParam MultiValueMap<String, String> data) {
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("userId")));
        String botCode = data.getFirst("botCode");
        String input = data.getFirst("input");
        return botRunningService.addBot(userId, botCode, input);
    }
}
