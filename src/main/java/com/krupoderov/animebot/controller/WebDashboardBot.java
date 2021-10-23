package com.krupoderov.animebot.controller;

import com.krupoderov.animebot.config.BotConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebDashboardBot {

    private final BotConfig botConfig;

    public WebDashboardBot(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @GetMapping(value = "/")
    public String hello() {
        return "Success: " + botConfig.getBotUserName();
    }
}
