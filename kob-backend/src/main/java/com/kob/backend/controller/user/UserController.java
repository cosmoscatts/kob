package com.kob.backend.controller.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kotlin.Result;

@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("/account/token")
    public Result<?> getToken() {
        return null;
    }

    @GetMapping("/account/register")
    public Result<?> register() {
        return null;
    }

    @GetMapping("/account/info")
    public Result<?> getInfo() {
        return null;
    }
}
