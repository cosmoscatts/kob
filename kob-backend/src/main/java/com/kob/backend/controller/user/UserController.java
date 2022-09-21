package com.kob.backend.controller.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kob.backend.dataobject.UserDO;

@RestController
public class UserController {

    @GetMapping("/user")
    public UserDO getUser() {
        return null;
    }
}
