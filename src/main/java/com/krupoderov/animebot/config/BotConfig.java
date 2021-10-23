package com.krupoderov.animebot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("classpath:application.properties")
public class BotConfig {

    //Bot name specified during registration
    @Value("${botUserName}")
    String botUserName;

    //Bot token received during registration
    @Value("${token}")
    String token;
}
