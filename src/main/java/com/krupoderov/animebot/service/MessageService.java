package com.krupoderov.animebot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public interface MessageService {
    SendMessage getStartMessage(long chatId);

    SendMessage getMessage(long chatId, String text);

    EditMessageText getNsfwEditMessageByCallback(String callback, long chatId, int messageId);

    EditMessageText getEditMessage(long messageId,
                                   long chatId,
                                   String message,
                                   String firstButtonText,
                                   String firstButtonCallback,
                                   String secondButtonText,
                                   String secondButtonCallback,
                                   String thirdButtonText,
                                   String thirdButtonCallback,
                                   String fourButtonText,
                                   String fourButtonCallback,
                                   String fiveButtonText,
                                   String fiveButtonCallback,
                                   String sixButtonText,
                                   String sixButtonCallback);

    EditMessageText getEditMessage(long messageId,
                                   long chatId,
                                   String message,
                                   String firstButtonText,
                                   String firstButtonCallback,
                                   String secondButtonText,
                                   String secondButtonCallback,
                                   String thirdButtonText,
                                   String thirdButtonCallback,
                                   String fourButtonText,
                                   String fourButtonCallback,
                                   String fiveButtonText,
                                   String fiveButtonCallback);

    SendMessage getMessageByUrl(long chatId, String imageName, String description, String callbackData);
}
