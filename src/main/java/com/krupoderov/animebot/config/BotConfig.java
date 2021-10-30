package com.krupoderov.animebot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("classpath:application.yml")
public class BotConfig {

    //Bot name specified during registration
    @Value("${bot.configuration.username}")
    String botUserName;

    //Bot token received during registration
    @Value("${bot.configuration.token}")
    String token;

    //Chat id of the administrator, we get on the command /getChatId
    @Value("${bot.configuration.admin}")
    String adminId;

    //Chat id of the owner, we get on the command /getChatId
    @Value("${bot.configuration.owner}")
    public String ownerId;
}
