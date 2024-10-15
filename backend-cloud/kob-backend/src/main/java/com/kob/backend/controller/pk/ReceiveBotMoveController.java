package com.kob.backend.controller.pk;

import com.kob.backend.service.PkService;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

@RestController
@RequestMapping("/pk/receive")
public class ReceiveBotMoveController {
    @Resource
    private PkService pkService;

    @PostMapping("/bot/move/")
    public String receiveBotMove(@RequestParam MultiValueMap<String, String> data) {
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("userId")));
        Integer direction = Integer.parseInt(Objects.requireNonNull(data.getFirst("direction")));
        return pkService.receiveBotMove(userId, direction);
    }
}
