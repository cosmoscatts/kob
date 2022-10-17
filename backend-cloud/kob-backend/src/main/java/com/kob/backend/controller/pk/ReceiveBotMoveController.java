package com.kob.backend.controller.pk;

import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kob.backend.biz.pk.ReceiveBotMoveBiz;

@RestController
@RequestMapping("/pk/receive")
public class ReceiveBotMoveController {
    @Resource
    private ReceiveBotMoveBiz receiveBotMoveBiz;

    @PostMapping("/bot/move/")
    public String receiveBotMove(@RequestParam MultiValueMap<String, String> data) {
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("userId")));
        Integer direction = Integer.parseInt(Objects.requireNonNull(data.getFirst("direction")));
        return receiveBotMoveBiz.receiveBotMove(userId, direction);
    }
}
