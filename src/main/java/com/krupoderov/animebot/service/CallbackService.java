package com.krupoderov.animebot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.io.IOException;

public interface CallbackService {

    SendMessage getMessage(String callback, long chatId);

    EditMessageText getEditMessage(String callback, long chatId, int messageId);

    SendPhoto getPhoto(String callback, long chatId) throws IOException;
}
