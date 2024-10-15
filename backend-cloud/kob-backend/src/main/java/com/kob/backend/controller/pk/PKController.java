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
@RequestMapping("/pk")
public class PKController {
    @Resource
    private PkService pkService;

    @PostMapping("/start/game")
    public String startGame(@RequestParam MultiValueMap<String, String> data) {
        Integer aId = Integer.parseInt(Objects.requireNonNull(data.getFirst("aId")));
        Integer aBotId = Integer.parseInt(Objects.requireNonNull(data.getFirst("aBotId")));
        Integer bId = Integer.parseInt(Objects.requireNonNull(data.getFirst("bId")));
        Integer bBotId = Integer.parseInt(Objects.requireNonNull(data.getFirst("bBotId")));
        pkService.startGame(aId, aBotId, bId, bBotId);
        return null;
    }
}
