package com.krupoderov.animebot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

public interface ButtonService {

    void setButtons(SendMessage message, String buttonName, String callbackData);

    void setButtons(SendPhoto message, String buttonName, String callbackData);

    void setButtons(SendPhoto message,
                    String firstButtonText,
                    String secondButtonText,
                    String firstButtonCallback,
                    String secondButtonCallback);

    void setButtons(SendPhoto message,
                    String firstButtonText,
                    String secondButtonText,
                    String thirdButtonText,
                    String firstButtonCallback,
                    String secondButtonCallback,
                    String thirdButtonCallback);
}
