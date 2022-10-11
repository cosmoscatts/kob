package com.kob.backend.controller.pk;

import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kob.backend.biz.pk.StartGameBiz;

@RestController
@RequestMapping("/pk")
public class PKController {
    @Resource
    private StartGameBiz startGameBiz;

    @PostMapping("/start/game")
    public String startGame(@RequestParam MultiValueMap<String, String> data) {
        Integer aId = Integer.parseInt(Objects.requireNonNull(data.getFirst("aId")));
        Integer bId = Integer.parseInt(Objects.requireNonNull(data.getFirst("bId")));
        startGameBiz.startGame(aId, bId);
        return null;
    }
}